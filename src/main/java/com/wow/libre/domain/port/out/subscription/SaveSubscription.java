package com.wow.libre.domain.port.out.subscription;

import com.wow.libre.infrastructure.entities.*;

public interface SaveSubscription {
    void save(SubscriptionEntity subscription, String transactionId);
}
