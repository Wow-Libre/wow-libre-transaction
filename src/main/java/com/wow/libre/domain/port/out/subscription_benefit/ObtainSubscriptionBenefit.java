package com.wow.libre.domain.port.out.subscription_benefit;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainSubscriptionBenefit {
    Optional<SubscriptionBenefitEntity> findByUserIdAndBenefitIdAndServerId(Long userId, Long benefitId, Long serverId);
}
