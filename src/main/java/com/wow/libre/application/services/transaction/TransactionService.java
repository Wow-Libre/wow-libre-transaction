package com.wow.libre.application.services.transaction;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.payment_gateway.*;
import com.wow.libre.domain.port.in.product.*;
import com.wow.libre.domain.port.in.transaction.*;
import com.wow.libre.domain.port.out.plan.*;
import com.wow.libre.domain.port.out.transaction.*;
import com.wow.libre.infrastructure.entities.*;
import com.wow.libre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class TransactionService implements TransactionPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private final ObtainTransaction obtainTransaction;
    private final SaveTransaction saveTransaction;
    private final ProductPort productPort;
    private final RandomString randomString;
    private final ObtainPlan obtainPlan;
    private final PaymentGatewayPort paymentGatewayPort;

    public TransactionService(ObtainTransaction obtainTransaction, SaveTransaction saveTransaction,
                              ProductPort productPort, @Qualifier("subscription-reference") RandomString randomString,
                              ObtainPlan obtainPlan, PaymentGatewayPort paymentGatewayPort) {
        this.obtainTransaction = obtainTransaction;
        this.saveTransaction = saveTransaction;
        this.productPort = productPort;
        this.randomString = randomString;
        this.obtainPlan = obtainPlan;
        this.paymentGatewayPort = paymentGatewayPort;
    }

    @Override
    public void save(TransactionEntity transaction, String transactionId) {
        saveTransaction.save(transaction, transactionId);
    }

    @Override
    public PaymentApplicableModel isRealPaymentApplicable(TransactionModel transaction, String transactionId) {

        final String orderId = randomString.nextString();

        if (transaction.isSubscription()) {
            String productReference = transaction.getProductReference();

            Optional<PlanEntity> planDetailDto = obtainPlan.findById(Long.valueOf(productReference), transactionId);

            if (planDetailDto.isEmpty()) {
                LOGGER.error("There is no active plan.  transactionId: {}", transaction);
                throw new InternalException("There is no active plan.", transactionId);
            }

            PlanEntity plan = planDetailDto.get();
            double discountPercentage = plan.getDiscount() / 100.0;
            double discountedPrice = plan.getPrice() * (1 - discountPercentage);
            final String currency = plan.getCurrency();
            final String description = String.format("Subscription %s", plan.getName());

            TransactionEntity transactionEntity = new TransactionEntity();
            transactionEntity.setUserId(transaction.getUserId());
            transactionEntity.setAccountId(transaction.getAccountId());
            transactionEntity.setRealmId(transaction.getRealmId());
            transactionEntity.setPrice(discountedPrice);
            transactionEntity.setStatus(TransactionStatus.CREATED.getType());
            transactionEntity.setProductId(null);
            transactionEntity.setSubscriptionId(null);
            transactionEntity.setReferenceNumber(orderId);
            transactionEntity.setCreditPoints(false);
            transactionEntity.setSend(false);
            transactionEntity.setCreationDate(LocalDateTime.now());
            transactionEntity.setCurrency(currency);
            transactionEntity.setPlanId(plan.getId());
            transactionEntity.setSubscription(true);
            transactionEntity.setPaymentMethod(transaction.getPaymentType().getType());
            saveTransaction.save(transactionEntity, transactionId);

            return new PaymentApplicableModel(discountedPrice > 0, discountedPrice, currency, orderId, description,
                    plan.getTax(),
                    plan.getReturnTax(), plan.getName(), transactionEntity);
        }

        String productReference = transaction.getProductReference();

        ProductEntity productDto = productPort.getProduct(productReference, transactionId);

        if (productDto == null) {
            LOGGER.error("Product not found productReferenceNumber: {} transactionId: {}", productReference,
                    transaction);
            throw new InternalException("The product is not available for donation", transactionId);
        }

        final String description = productDto.getName();
        final boolean hasCreditPoints = productDto.isUseCreditPoints();
        Double price = productDto.getPrice();
        Integer discountPercentage = productDto.getDiscount();
        double discountAmount = ((double) discountPercentage / 100) * price;
        final Double finalPrice = price - discountAmount;
        final String currency = hasCreditPoints ? "POINTS" : "USD";
        final boolean isFree = finalPrice <= 0;

        final boolean isPaymentRedirectToCheckout = !isFree && !hasCreditPoints;

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccountId(transaction.getAccountId());
        transactionEntity.setRealmId(productDto.getRealmId());
        transactionEntity.setAccountId(transaction.getAccountId());
        transactionEntity.setStatus(TransactionStatus.CREATED.getType());
        transactionEntity.setProductId(productDto);
        transactionEntity.setCreationDate(LocalDateTime.now());
        transactionEntity.setReferenceNumber(orderId);
        transactionEntity.setPrice(finalPrice);
        transactionEntity.setSend(false);
        transactionEntity.setCurrency(currency);
        transactionEntity.setUserId(transaction.getUserId());
        transactionEntity.setPaymentMethod(transaction.getPaymentType().getType());
        saveTransaction.save(transactionEntity, transactionId);

        return new PaymentApplicableModel(isPaymentRedirectToCheckout, finalPrice, currency, orderId, description,
                productDto.getTax(), productDto.getReturnTax(), productDto.getName(), transactionEntity);
    }

    private TransactionEntity getTransactionEntity(String reference, String transactionId) {
        Optional<TransactionEntity> transactionEntity = obtainTransaction.findByReferenceNumber(reference,
                transactionId);

        if (transactionEntity.isEmpty()) {
            throw new InternalException("", transactionId);
        }

        return transactionEntity.get();
    }

    @Override
    public TransactionEntity transaction(String reference, String transactionId) {
        return getTransactionEntity(reference, transactionId);
    }

    @Override
    public TransactionsDto transactionsByUserId(Long userId, Integer page, Integer size, String transactionId) {
        TransactionsDto data = new TransactionsDto();
        List<Transaction> transactions = obtainTransaction.findByUserId(userId, page, size, transactionId)
                .stream()
                .map(transaction -> new Transaction(transaction.getId(), transaction.getPrice(),
                        transaction.getCurrency(), transaction.getStatus(),
                        TransactionStatus.getType(transaction.getStatus()).getStatus(),
                        transaction.getCreationDate(), transaction.getReferenceNumber(),
                        Optional.ofNullable(transaction.getProductId())
                                .map(ProductEntity::getName).orElse("VIP"),
                        Optional.ofNullable(transaction.getProductId())
                                .map(ProductEntity::getImageUrl).orElse("https://static.wixstatic" +
                                        ".com/media/5dd8a0_cbcd4683525e448c8502b031dfce2527~mv2.webp")))
                .toList();
        data.setTransactions(transactions);
        data.setSize(obtainTransaction.findByUserId(userId, transactionId));

        return data;
    }


    @Override
    public Optional<TransactionEntity> findByReferenceNumber(String referenceNumber, String transactionId) {
        return obtainTransaction.findByReferenceNumber(referenceNumber, transactionId);
    }

    @Override
    public Optional<TransactionEntity> findByReferenceNumberAndUserId(String referenceNumber, Long userId,
                                                                      String transactionId) {
        Optional<TransactionEntity> transaction = obtainTransaction.findByReferenceNumberAndUserId(referenceNumber,
                userId, transactionId);

        if (transaction.isEmpty()) {
            return Optional.empty();
        }

        TransactionEntity foundTransaction = transaction.get();

        boolean statusPaid = foundTransaction.getStatus().equalsIgnoreCase(TransactionStatus.PAID.getType())
                || foundTransaction.getStatus().equalsIgnoreCase(TransactionStatus.DELIVERED.getType());

        if (statusPaid) {
            return Optional.of(foundTransaction);
        }

        PaymentType paymentMethodType = PaymentType.valueOf(foundTransaction.getPaymentMethod());

        PaymentStatus paymentStatus = paymentGatewayPort.findByStatus(paymentMethodType,
                foundTransaction.getReferenceNumber(), foundTransaction.getReferencePayment(),
                transactionId);

        // Mapear el PaymentStatus a TransactionStatus
        switch (paymentStatus) {
            case APPROVED:
                if (!foundTransaction.getStatus().equalsIgnoreCase(TransactionStatus.PAID.getType())) {
                    foundTransaction.setStatus(TransactionStatus.PAID.getType());
                    LOGGER.info("✅ Pago aprobado para transacción: {}", foundTransaction.getReferenceNumber());
                }
                break;
            case PENDING:
                foundTransaction.setStatus(TransactionStatus.PENDING.getType());
                LOGGER.info("⏳ Pago pendiente para transacción: {}", foundTransaction.getReferenceNumber());
                break;
            case REJECTED:
                foundTransaction.setStatus(TransactionStatus.REJECTED.getType());
                LOGGER.warn("❌ Pago rechazado para transacción: {}", foundTransaction.getReferenceNumber());
                break;
            default:
                foundTransaction.setStatus(TransactionStatus.REJECTED.getType());
                LOGGER.warn("⚠️ Status de pago desconocido: {} para transacción: {}",
                        paymentStatus, foundTransaction.getReferenceNumber());
                break;
        }

        // Guardar la transacción actualizada
        saveTransaction.save(foundTransaction, transactionId);

        return Optional.of(foundTransaction);
    }

}
