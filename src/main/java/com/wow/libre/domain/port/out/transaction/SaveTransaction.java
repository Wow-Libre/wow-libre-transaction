package com.wow.libre.domain.port.out.transaction;

import com.wow.libre.infrastructure.entities.*;

public interface SaveTransaction {
    void save(TransactionEntity transactionEntity, String transactionId);
}
