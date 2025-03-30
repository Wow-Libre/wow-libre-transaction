package com.wow.libre.domain.port.in.subscription;

import com.wow.libre.domain.dto.*;
import com.wow.libre.infrastructure.entities.*;

public interface SubscriptionPort {

    PillWidgetHomeDto getPillHome(Long userId, String language, String transactionId);

    SubscriptionEntity createSubscription(Long userId, String transactionId);

    boolean isActiveSubscription(Long userId, String transactionId);

    SubscriptionBenefitsDto benefits(Long userId, Long serverId, String language, String transactionId);

    void claimBenefits(Long serverId, Long userId, Long accountId, Long characterId, String language, Long benefitId,
                       String transactionId);

}
