package com.wow.libre.domain.port.out.subscription;

import com.wow.libre.infrastructure.entities.*;

public interface SaveSubscription {
    SubscriptionEntity save(SubscriptionEntity subscription, String transactionId);
}
