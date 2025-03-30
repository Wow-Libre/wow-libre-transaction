package com.wow.libre.infrastructure.repositories.subscription;

import com.wow.libre.domain.port.out.subscription.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaSubscriptionAdapter implements SaveSubscription, ObtainSubscription {
    private final SubscriptionRepository subscriptionRepository;

    public JpaSubscriptionAdapter(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public SubscriptionEntity save(SubscriptionEntity subscription, String transactionId) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Optional<SubscriptionEntity> findByUserIdAndStatus(Long userId, String status) {
        return subscriptionRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public Optional<SubscriptionEntity> findByReferenceNumber(String reference) {
        return subscriptionRepository.findByReferenceNumber(reference);
    }
}
