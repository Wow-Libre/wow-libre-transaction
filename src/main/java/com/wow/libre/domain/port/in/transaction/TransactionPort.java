package com.wow.libre.domain.port.in.transaction;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface TransactionPort {

    void save(TransactionEntity transaction, String transactionId);

    PaymentApplicableModel isRealPaymentApplicable(TransactionModel transaction, String transactionId);

    TransactionEntity transaction(String reference, String transactionId);

    TransactionsDto transactionsByUserId(Long userId, Integer page, Integer size, String transactionId);

    List<TransactionEntity> findByStatusIsPaidAndSendIsFalse(String transactionId);

    Optional<TransactionEntity> findByReferenceNumber(String referenceNumber, String transactionId);
}
