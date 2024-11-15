package com.wow.libre.application.services.subscription;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.subscription.*;
import com.wow.libre.domain.port.in.wowlibre.*;
import com.wow.libre.domain.port.out.resources.*;
import com.wow.libre.domain.port.out.subscription.*;
import com.wow.libre.domain.port.out.subscription_benefit.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class SubscriptionService implements SubscriptionPort {
    private final ObtainSubscription obtainSubscription;
    private final ObtainJsonLoader obtainJsonLoader;
    private final SaveSubscription saveSubscription;
    private final ObtainSubscriptionBenefit obtainSubscriptionBenefit;
    private final SaveSubscriptionBenefit saveSubscriptionBenefit;
    private final WowLibrePort wowLibrePort;

    public SubscriptionService(ObtainSubscription obtainSubscription, ObtainJsonLoader obtainJsonLoader,
                               SaveSubscription saveSubscription, ObtainSubscriptionBenefit obtainSubscriptionBenefit
            , SaveSubscriptionBenefit saveSubscriptionBenefit
            , WowLibrePort wowLibrePort) {
        this.obtainSubscription = obtainSubscription;
        this.obtainJsonLoader = obtainJsonLoader;
        this.saveSubscription = saveSubscription;
        this.obtainSubscriptionBenefit = obtainSubscriptionBenefit;
        this.saveSubscriptionBenefit = saveSubscriptionBenefit;
        this.wowLibrePort = wowLibrePort;
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

    @Override
    public SubscriptionCreateDto createSubscription(Long userId, String transactionId) {

        Optional<SubscriptionEntity> subscriptionEntity = obtainSubscription.findByUserIdAndStatus(userId,
                SubscriptionStatus.ACTIVE.getType());

        if (subscriptionEntity.isPresent()) {
            throw new InternalException("", transactionId);
        }

        SubscriptionEntity subscription = new SubscriptionEntity();

        return null;
    }

    @Override
    public boolean isActiveSubscription(Long userId, String transactionId) {
        Optional<SubscriptionEntity> subscriptionEntity = obtainSubscription.findByUserIdAndStatus(userId,
                SubscriptionStatus.ACTIVE.getType());

        return subscriptionEntity.isPresent();
    }

    @Override
    public SubscriptionBenefitsDto benefits(Long userId, String language, String transactionId) {

        if (!isActiveSubscription(userId, transactionId)) {
            return new SubscriptionBenefitsDto(new ArrayList<>(), 0);
        }
        List<SubscriptionBenefitDto> benefits =
                obtainJsonLoader.getBenefitsPremium(language, transactionId).stream()
                        .filter(benefit -> benefit.reactivable
                                || obtainSubscriptionBenefit.findByUserIdAndBenefitId(userId, benefit.id).isEmpty()).toList();

        return new SubscriptionBenefitsDto(benefits, benefits.size());
    }

    @Override
    public void claimBenefits(Long serverId, Long userId, Long accountId, Long characterId, String language,
                              Long benefitId,
                              String transactionId) {

        if (!isActiveSubscription(userId, transactionId)) {
            throw new InternalException("", transactionId);
        }

        final String jwt = wowLibrePort.getJwt(transactionId);

        Optional<SubscriptionBenefitDto> benefits =
                obtainJsonLoader.getBenefitsPremium(language, transactionId).stream()
                        .filter(benefit -> Objects.equals(benefit.id, benefitId)).findAny();

        if (benefits.isEmpty()) {
            throw new InternalException("", transactionId);
        }
        SubscriptionBenefitDto benefitModel = benefits.get();
        if (!benefitModel.reactivable) {
            if (obtainSubscriptionBenefit.findByUserIdAndBenefitId(userId, benefitModel.id).isPresent()) {
                throw new InternalException("", transactionId);
            }
            SubscriptionBenefitEntity subscriptionBenefit = new SubscriptionBenefitEntity();
            subscriptionBenefit.setBenefitId(benefitModel.id);
            subscriptionBenefit.setCreatedAt(new Date());
            subscriptionBenefit.setUserId(userId);
            saveSubscriptionBenefit.save(subscriptionBenefit);
        }


        wowLibrePort.sendBenefitsPremium(jwt, serverId, userId, accountId, characterId, null, benefitModel.type,
                benefitModel.amount,
                transactionId);

    }


}
