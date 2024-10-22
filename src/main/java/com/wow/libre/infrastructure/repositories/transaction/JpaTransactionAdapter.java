package com.wow.libre.infrastructure.repositories.transaction;

import com.wow.libre.domain.port.out.transaction.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

@Repository
public class JpaTransactionAdapter implements SaveTransaction {
    private final TransactionRepository transactionRepository;

    public JpaTransactionAdapter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void save(TransactionEntity transactionEntity, String transactionId) {
        transactionRepository.save(transactionEntity);
    }
}
