package com.wow.libre.infrastructure.repositories.transaction;


import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.port.out.transaction.*;
import com.wow.libre.infrastructure.entities.*;
import jakarta.persistence.*;
import org.slf4j.*;
import org.springframework.data.domain.*;
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
    public List<TransactionEntity> findByUserId(Long userId, int page, int size, String transactionId) {
        return transactionRepository.findByUserId(userId, PageRequest.of(page, size)).stream().toList();
    }

    @Override
    public Long findByUserId(Long userId, String transactionId) {
        return transactionRepository.countByUserId(userId);
    }

    @Override
    public List<TransactionEntity> findByStatusIsPaidAndSendIsFalse(String transactionId) {
        return transactionRepository.findByStatusAndSendIsFalse(TransactionStatus.PAID.getType());
    }

    @Override
    public Optional<TransactionEntity> findById(Long id) {
        return transactionRepository.findById(id);
    }
}
