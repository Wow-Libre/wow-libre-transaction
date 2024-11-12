package com.wow.libre.domain.port.out.subscription;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainSubscription {
    Optional<SubscriptionEntity> findByUserIdAndStatus(Long userId, String status);

    Optional<SubscriptionEntity> findByReferenceNumber(String reference);
}
