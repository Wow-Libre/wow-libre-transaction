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
    public Optional<SubscriptionBenefitEntity> findByUserIdAndBenefitIdAndServerId(Long userId, Long benefitId,
                                                                                   Long serverId) {
        return subscriptionBenefitRepository.findByUserIdAndBenefitIdAndServerId(userId, benefitId, serverId);
    }

    @Override
    public void save(SubscriptionBenefitEntity subscriptionBenefit) {
        subscriptionBenefitRepository.save(subscriptionBenefit);
    }
}
