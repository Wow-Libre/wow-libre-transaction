package com.wow.libre.infrastructure.repositories.transaction;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByReferenceNumber(String referenceNumber);

    Optional<TransactionEntity> findByPaymentId(String paymentId);

}
