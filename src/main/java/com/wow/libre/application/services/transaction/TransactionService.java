package com.wow.libre.application.services.transaction;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
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

    public TransactionService(ObtainTransaction obtainTransaction, SaveTransaction saveTransaction,
                              ProductPort productPort, @Qualifier("subscription-reference") RandomString randomString,
                              ObtainPlan obtainPlan) {
        this.obtainTransaction = obtainTransaction;
        this.saveTransaction = saveTransaction;
        this.productPort = productPort;
        this.randomString = randomString;
        this.obtainPlan = obtainPlan;
    }


    @Override
    public void save(TransactionEntity transaction, String transactionId) {
        saveTransaction.save(transaction, transactionId);
    }

    @Override
    public PaymentApplicableModel isRealPaymentApplicable(TransactionModel transaction, String transactionId) {

        final String orderId = randomString.nextString();

        if (transaction.isSubscription()) {

            Optional<PlanEntity> planDetailDto = obtainPlan.findByStatusIsTrue(transactionId);

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
            transactionEntity.setServerId(transaction.getServerId());
            transactionEntity.setPrice(discountedPrice);
            transactionEntity.setStatus(TransactionStatus.CREATED.getType());
            transactionEntity.setProductId(null);
            transactionEntity.setSubscriptionId(null);
            transactionEntity.setReferenceNumber(orderId);
            transactionEntity.setCreditPoints(false);
            transactionEntity.setSend(false);
            transactionEntity.setCreationDate(LocalDateTime.now());
            transactionEntity.setCurrency(currency);
            transactionEntity.setSubscription(true);
            saveTransaction.save(transactionEntity, transactionId);

            return new PaymentApplicableModel(true, discountedPrice, currency, orderId, description, plan.getTax(),
                    plan.getReturnTax());
        }

        String productReference = transaction.getProductReference();

        ProductEntity productDto = productPort.getProduct(productReference, transactionId);

        if (productDto == null) {
            LOGGER.error("Product not found productReferenceNumber: {} transactionId: {}", productReference,
                    transaction);
            throw new InternalException("The product is not available for donation", transactionId);
        }

        final String description = productDto.getName();
        final boolean creditPoints = productDto.isUseCreditPoints();
        Double price = productDto.getPrice();
        Integer discountPercentage = productDto.getDiscount();
        double discountAmount = ((double) discountPercentage / 100) * price;
        final Double finalPrice = price - discountAmount;
        final String currency = creditPoints ? "POINTS" : "USD";
        final boolean isPaymentRedirectToCheckout = !creditPoints;

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccountId(transaction.getAccountId());
        transactionEntity.setServerId(productDto.getPartnerId().getId());
        transactionEntity.setAccountId(transaction.getAccountId());
        transactionEntity.setStatus(TransactionStatus.CREATED.getType());
        transactionEntity.setProductId(productDto);
        transactionEntity.setCreationDate(LocalDateTime.now());
        transactionEntity.setReferenceNumber(orderId);
        transactionEntity.setPrice(finalPrice);
        transactionEntity.setSend(false);
        transactionEntity.setCurrency(currency);
        transactionEntity.setUserId(transaction.getUserId());
        saveTransaction.save(transactionEntity, transactionId);

        return new PaymentApplicableModel(isPaymentRedirectToCheckout, finalPrice, currency, orderId, description,
                productDto.getTax(),
                productDto.getReturnTax());
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
        List<Transaction> transactions =
                obtainTransaction.findByUserId(userId, page, size, transactionId)
                        .stream().map(transaction -> new Transaction(transaction.getId(), transaction.getPrice(),
                                transaction.getCurrency(), transaction.getStatus(),
                                TransactionStatus.getType(transaction.getStatus()).getStatus(),
                                transaction.getCreationDate(), transaction.getReferenceNumber(),
                                Optional.ofNullable(transaction.getProductId())
                                        .map(ProductEntity::getName).orElse("VIP"),
                                Optional.ofNullable(transaction.getProductId())
                                        .map(ProductEntity::getImageUrl).orElse("https://static.wixstatic" +
                                                ".com/media/5dd8a0_cbcd4683525e448c8502b031dfce2527~mv2.webp"))).toList();
        data.setTransactions(transactions);
        data.setSize(obtainTransaction.findByUserId(userId, transactionId));

        return data;
    }

    @Override
    public List<TransactionEntity> findByStatusIsPaidAndSendIsFalse(String transactionId) {
        return obtainTransaction.findByStatusIsPaidAndSendIsFalse(transactionId);
    }

    @Override
    public Optional<TransactionEntity> findByReferenceNumber(String referenceNumber, String transactionId) {
        return obtainTransaction.findByReferenceNumber(referenceNumber, transactionId);
    }

}
