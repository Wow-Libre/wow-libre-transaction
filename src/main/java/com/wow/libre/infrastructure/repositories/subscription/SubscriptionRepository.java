package com.wow.libre.infrastructure.repositories.subscription;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, Long> {

    Optional<SubscriptionEntity> findByUserIdAndStatus(Long userId, String status);

    Optional<SubscriptionEntity> findByReferenceNumber(String referenceNumber);
}
