package com.wow.libre.infrastructure.repositories.transaction;

import com.wow.libre.domain.port.out.transaction.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaTransactionAdapter implements SaveTransaction, ObtainTransaction {
    private final TransactionRepository transactionRepository;

    public JpaTransactionAdapter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void save(TransactionEntity transactionEntity, String transactionId) {
        transactionRepository.save(transactionEntity);
    }

    @Override
    public Optional<TransactionEntity> findByReferenceNumber(String reference, String transactionId) {
        return transactionRepository.findByReferenceNumber(reference);
    }

    @Override
    public Optional<TransactionEntity> findByPaymentId(String paymentId, String transactionId) {
        return transactionRepository.findByPaymentId(paymentId);
    }
}
