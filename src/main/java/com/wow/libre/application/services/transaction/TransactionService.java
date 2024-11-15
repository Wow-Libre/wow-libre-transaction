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
    public static final String AZEROTH_PASS = "Azeroth Pass";
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private final ObtainTransaction obtainTransaction;
    private final SaveTransaction saveTransaction;
    private final ProductPort productPort;
    private final RandomString randomString;
    private final ObtainPlan obtainPlan;

    public TransactionService(ObtainTransaction obtainTransaction, SaveTransaction saveTransaction,
                              ProductPort productPort,
                              @Qualifier("random-string") RandomString randomString, ObtainPlan obtainPlan) {
        this.obtainTransaction = obtainTransaction;
        this.saveTransaction = saveTransaction;
        this.productPort = productPort;
        this.randomString = randomString;
        this.obtainPlan = obtainPlan;
    }

    @Override
    public Optional<TransactionEntity> findById(Long id) {
        return obtainTransaction.findById(id);
    }

    @Override
    public void save(TransactionEntity transaction, String transactionId) {
        saveTransaction.save(transaction, transactionId);
    }

    @Override
    public PaymentApplicableModel isRealPaymentApplicable(TransactionModel transaction, String transactionId) {

        final String productReferenceNumber = transaction.getReferenceNumber();
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
            final String description = String.format("Subscription %s", AZEROTH_PASS);

            TransactionEntity transactionEntity = new TransactionEntity();
            transactionEntity.setStatus(TransactionStatus.CREATED.getType());
            transactionEntity.setGold(false);
            transactionEntity.setReferenceNumber(orderId);
            transactionEntity.setPrice(discountedPrice);
            transactionEntity.setSend(false);
            transactionEntity.setCreationDate(LocalDateTime.now());
            transactionEntity.setCurrency(currency);
            transactionEntity.setUserId(transaction.getUserId());
            saveTransaction.save(transactionEntity, transactionId);

            return new PaymentApplicableModel(true, discountedPrice, currency, orderId, description,
                    plan.getSubscribeUrl());
        }

        ProductEntity productDto = productPort.getProduct(productReferenceNumber, transactionId);

        if (productDto == null) {
            LOGGER.error("Product not found productReferenceNumber: {} transactionId: {}", productReferenceNumber,
                    transaction);
            throw new InternalException("The product is not available for donation", transactionId);
        }

        boolean isPayment = !productDto.isGamblingMoney();
        final String description = String.format("Donation %s", productDto.getName());
        double price = isPayment ? productDto.getPrice() : productDto.getGoldPrice();
        Integer discountPercentage = productDto.getDiscount();
        double discountAmount = ((double) discountPercentage / 100) * price;
        Double finalPrice = price - discountAmount;
        final String currency = isPayment ? "USD" : "GOLD";

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccountId(transaction.getAccountId());
        transactionEntity.setStatus(isPayment ? TransactionStatus.CREATED.getType() :
                TransactionStatus.PENDING.getType());
        transactionEntity.setGold(!isPayment);
        transactionEntity.setProductId(productDto);
        transactionEntity.setServerId(productDto.getPartnerId().getId());
        transactionEntity.setCreationDate(LocalDateTime.now());
        transactionEntity.setReferenceNumber(orderId);
        transactionEntity.setPrice(finalPrice);
        transactionEntity.setSend(false);
        transactionEntity.setCurrency(currency);
        transactionEntity.setUserId(transaction.getUserId());
        saveTransaction.save(transactionEntity, transactionId);

        return new PaymentApplicableModel(isPayment, finalPrice, currency, orderId, description);
    }

    @Override
    public void assignmentPaymentId(String reference, String paymentId, String transactionId) {
        TransactionEntity transaction = getTransactionEntity(reference, transactionId);
        transaction.setPaymentId(paymentId);
        saveTransaction.save(transaction, transactionId);
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
                obtainTransaction.findByUserId(userId, page, size, transactionId).stream()
                        .map(transaction -> new Transaction(transaction.getId(), transaction.getPrice(),
                                transaction.getCurrency(), transaction.getStatus(),
                                TransactionStatus.getType(transaction.getStatus()).getStatus(),
                                transaction.getCreationDate(),
                                transaction.getReferenceNumber(),
                                Optional.ofNullable(transaction.getProductId()).map(ProductEntity::getName).orElse(""),
                                Optional.ofNullable(transaction.getProductId()).map(ProductEntity::getImageUrl).orElse(""))).toList();
        data.setTransactions(transactions);
        data.setSize(obtainTransaction.findByUserId(userId, transactionId));

        return data;
    }

    @Override
    public List<TransactionEntity> findByStatusIsPaidAndSendIsFalse(String transactionId) {
        return obtainTransaction.findByStatusIsPaidAndSendIsFalse(transactionId);
    }

}
