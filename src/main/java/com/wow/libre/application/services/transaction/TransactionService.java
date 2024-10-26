package com.wow.libre.application.services.transaction;

import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.product.*;
import com.wow.libre.domain.port.in.transaction.*;
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

    public TransactionService(ObtainTransaction obtainTransaction, SaveTransaction saveTransaction,
                              ProductPort productPort,
                              @Qualifier("random-string") RandomString randomString) {
        this.obtainTransaction = obtainTransaction;
        this.saveTransaction = saveTransaction;
        this.productPort = productPort;
        this.randomString = randomString;
    }

    @Override
    public void save(TransactionEntity transaction, String transactionId) {
        saveTransaction.save(transaction, transactionId);
    }

    @Override
    public PaymentApplicableModel isRealPaymentApplicable(TransactionModel transaction, String transactionId) {

        final String referenceNumber = transaction.getReferenceNumber();

        if (transaction.isSubscription()) {
            return null;
        }

        ProductEntity productDto = productPort.getProduct(referenceNumber, transactionId);

        if (productDto == null) {
            LOGGER.error("Product not found reference: {} transactionId: {}", referenceNumber, transaction);
            throw new InternalException("The product is not available for donation", transactionId);
        }

        boolean isPayment = !productDto.isGamblingMoney();
        final String orderId = randomString.nextString();

        Double price = productDto.getPrice();
        Integer discountPercentage = productDto.getDiscount();
        Double discountAmount = (discountPercentage / 100) * price;
        Double finalPrice = price - discountAmount;


        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccountId(transaction.getAccountId());
        transactionEntity.setStatus(isPayment ? "CREATED" : "PENDING");
        transactionEntity.setGold(!isPayment);
        transactionEntity.setProductId(productDto);
        transactionEntity.setServerId(transaction.getServerId());
        transactionEntity.setCreationDate(LocalDateTime.now());
        transactionEntity.setReferenceNumber(referenceNumber);
        transactionEntity.setPrice(finalPrice);
        transactionEntity.setUserId(transaction.getUserId());
        saveTransaction.save(transactionEntity, transactionId);

        return new PaymentApplicableModel(isPayment, finalPrice, "USD", orderId);
    }

    @Override
    public void assignmentPaymentId(String reference, String paymentId, String transactionId) {
        Optional<TransactionEntity> transactionEntity = obtainTransaction.findByReferenceNumber(reference,
                transactionId);

        if (transactionEntity.isEmpty()) {
            throw new InternalException("", transactionId);
        }

        TransactionEntity transaction = transactionEntity.get();
        transaction.setPaymentId(paymentId);
        saveTransaction.save(transaction, transactionId);
    }
}
