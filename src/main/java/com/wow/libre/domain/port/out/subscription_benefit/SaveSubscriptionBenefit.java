package com.wow.libre.domain.port.out.subscription_benefit;

import com.wow.libre.infrastructure.entities.*;

public interface SaveSubscriptionBenefit {
    void save(SubscriptionBenefitEntity subscriptionBenefit);
}
