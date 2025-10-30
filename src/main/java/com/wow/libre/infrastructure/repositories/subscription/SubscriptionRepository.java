package com.wow.libre.infrastructure.repositories.subscription;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.time.*;
import java.util.*;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, Long> {

    Optional<SubscriptionEntity> findByUserIdAndStatus(Long userId, String status);

    Optional<SubscriptionEntity> findByReferenceNumber(String referenceNumber);

    @Query("SELECT s FROM SubscriptionEntity s WHERE s.nextInvoiceDate < :now AND s.status = 'ACTIVE'")
    List<SubscriptionEntity> findExpiredActiveSubscriptions(LocalDateTime now);
}
