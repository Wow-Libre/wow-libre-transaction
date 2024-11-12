package com.wow.libre.application.services.subscription;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.subscription.*;
import com.wow.libre.domain.port.out.resources.*;
import com.wow.libre.domain.port.out.subscription.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class SubscriptionService implements SubscriptionPort {
    private final ObtainSubscription obtainSubscription;
    private final ObtainJsonLoader obtainJsonLoader;

    public SubscriptionService(ObtainSubscription obtainSubscription, ObtainJsonLoader obtainJsonLoader) {
        this.obtainSubscription = obtainSubscription;
        this.obtainJsonLoader = obtainJsonLoader;
    }

    @Override
    public PillWidgetHomeDto getPillHome(Long userId, String language, String transactionId) {
        PillHomeModel pillHomeModel = obtainJsonLoader.getResourcePillHome(language, transactionId);

        if (userId != null) {

            Optional<SubscriptionEntity> subscriptionEntity = obtainSubscription.findByUserIdAndStatus(userId,
                    SubscriptionStatus.ACTIVE.getType());

            if (subscriptionEntity.isPresent()) {
                return new PillWidgetHomeDto(pillHomeModel.getActive(), "/accounts");
            }

        }
        return new PillWidgetHomeDto(pillHomeModel.getInactive(), "/subscriptions");
    }
}
