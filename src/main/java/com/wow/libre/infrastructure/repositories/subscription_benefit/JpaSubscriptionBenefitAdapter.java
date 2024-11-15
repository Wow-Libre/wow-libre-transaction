package com.wow.libre.infrastructure.repositories.subscription_benefit;

import com.wow.libre.domain.port.out.subscription_benefit.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaSubscriptionBenefitAdapter implements ObtainSubscriptionBenefit, SaveSubscriptionBenefit {
    private final SubscriptionBenefitRepository subscriptionBenefitRepository;

    public JpaSubscriptionBenefitAdapter(SubscriptionBenefitRepository subscriptionBenefitRepository) {
        this.subscriptionBenefitRepository = subscriptionBenefitRepository;
    }

    @Override
    public Optional<SubscriptionBenefitEntity> findByUserIdAndBenefitId(Long userId, Long benefitId) {
        return subscriptionBenefitRepository.findByUserIdAndBenefitId(userId, benefitId);
    }

    @Override
    public void save(SubscriptionBenefitEntity subscriptionBenefit) {
        subscriptionBenefitRepository.save(subscriptionBenefit);
    }
}
