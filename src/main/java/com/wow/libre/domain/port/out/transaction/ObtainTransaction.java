package com.wow.libre.domain.port.out.transaction;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainTransaction {
    Optional<TransactionEntity> findByReferenceNumber(String reference, String transactionId);

    Optional<TransactionEntity> findByPaymentId(String paymentId, String transactionId);

}
