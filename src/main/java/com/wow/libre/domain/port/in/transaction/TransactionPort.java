package com.wow.libre.domain.port.in.transaction;

import com.wow.libre.domain.model.*;
import com.wow.libre.infrastructure.entities.*;

public interface TransactionPort {
    void save(TransactionEntity transaction, String transactionId);

    PaymentApplicableModel isRealPaymentApplicable(TransactionModel transaction, String transactionId);
}
