package com.wow.libre.infrastructure.repositories.transaction;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByReferenceNumber(String referenceNumber);

    Optional<TransactionEntity> findByPaymentId(String paymentId);

    Page<TransactionEntity> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(a) FROM TransactionEntity a WHERE a.userId = :userId")
    long countByUserId(@Param("userId") Long userId);

    List<TransactionEntity> findByStatusAndSendIsFalse(String status);


}
