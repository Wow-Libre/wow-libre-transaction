package com.wow.libre.domain.port.in.transaction;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.infrastructure.entities.*;

import javax.swing.text.html.*;
import java.util.*;

public interface TransactionPort {
    Optional<TransactionEntity> findById(Long id);

    void save(TransactionEntity transaction, String transactionId);

    PaymentApplicableModel isRealPaymentApplicable(TransactionModel transaction, String transactionId);

    void assignmentPaymentId(String reference, String paymentId, String transactionId);

    TransactionEntity transaction(String reference, String transactionId);

    TransactionsDto transactionsByUserId(Long userId, Integer page, Integer size, String transactionId);

    List<TransactionEntity> findByStatusIsPaidAndSendIsFalse(String transactionId);
}
