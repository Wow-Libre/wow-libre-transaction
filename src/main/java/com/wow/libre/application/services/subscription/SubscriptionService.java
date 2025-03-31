package com.wow.libre.application.services.subscription;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.subscription.*;
import com.wow.libre.domain.port.in.wowlibre.*;
import com.wow.libre.domain.port.out.plan.*;
import com.wow.libre.domain.port.out.resources.*;
import com.wow.libre.domain.port.out.subscription.*;
import com.wow.libre.domain.port.out.subscription_benefit.*;
import com.wow.libre.infrastructure.entities.*;
import com.wow.libre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class SubscriptionService implements SubscriptionPort {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SubscriptionService.class);

    private final ObtainSubscription obtainSubscription;
    private final ObtainJsonLoader obtainJsonLoader;
    private final ObtainSubscriptionBenefit obtainSubscriptionBenefit;
    private final SaveSubscriptionBenefit saveSubscriptionBenefit;
    private final SaveSubscription saveSubscription;
    private final ObtainPlan obtainPlan;
    private final WowLibrePort wowLibrePort;
    private final RandomString randomString;

    public SubscriptionService(ObtainSubscription obtainSubscription, ObtainJsonLoader obtainJsonLoader,
                               ObtainSubscriptionBenefit obtainSubscriptionBenefit
            , SaveSubscriptionBenefit saveSubscriptionBenefit
            , WowLibrePort wowLibrePort, SaveSubscription saveSubscription, ObtainPlan obtainPlan,
                               @Qualifier("product-reference") RandomString randomString) {
        this.obtainSubscription = obtainSubscription;
        this.obtainJsonLoader = obtainJsonLoader;
        this.obtainSubscriptionBenefit = obtainSubscriptionBenefit;
        this.saveSubscriptionBenefit = saveSubscriptionBenefit;
        this.wowLibrePort = wowLibrePort;
        this.saveSubscription = saveSubscription;
        this.obtainPlan = obtainPlan;
        this.randomString = randomString;
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
    public SubscriptionEntity createSubscription(Long userId, String transactionId) {

        Optional<SubscriptionEntity> subscriptionEntity = obtainSubscription.findByUserIdAndStatus(userId,
                SubscriptionStatus.ACTIVE.getType());

        if (subscriptionEntity.isPresent()) {
            throw new InternalException("You already have an active subscription", transactionId);
        }

        Optional<PlanEntity> planActive = obtainPlan.findByStatusIsTrue(transactionId);

        if (planActive.isEmpty()) {
            LOGGER.error("There is no active plan and the transaction could not be completed UserId {} " +
                            "TransactionId {}", userId,
                    transactionId);
            throw new InternalException("There is no active plan", transactionId);
        }

        PlanEntity plan = planActive.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextInvoiceDate = now.plusDays(30);

        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setUserId(userId);
        subscription.setPlanId(plan);
        subscription.setCreationDate(LocalDateTime.now());
        subscription.setNextInvoiceDate(nextInvoiceDate);
        subscription.setReferenceNumber(randomString.nextString());
        subscription.setStatus(SubscriptionStatus.ACTIVE.getType());
        return saveSubscription.save(subscription, transactionId);
    }

    @Override
    public boolean isActiveSubscription(Long userId, String transactionId) {
        Optional<SubscriptionEntity> subscriptionEntity = obtainSubscription.findByUserIdAndStatus(userId,
                SubscriptionStatus.ACTIVE.getType());

        return subscriptionEntity.isPresent();
    }

    @Override
    public SubscriptionBenefitsDto benefits(Long userId, Long serverId, String language, String transactionId) {

        if (!isActiveSubscription(userId, transactionId)) {
            return new SubscriptionBenefitsDto(new ArrayList<>(), 0);
        }

        List<SubscriptionBenefitDto> benefits =
                obtainJsonLoader.getBenefitsPremium(language, transactionId).stream()
                        .filter(benefit -> serverId.equals(benefit.getServerId()) && (benefit.getReactivable()
                                || obtainSubscriptionBenefit.findByUserIdAndBenefitIdAndServerId(userId,
                                benefit.getId(), serverId).isEmpty())).toList();

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
                        .filter(benefit -> Objects.equals(benefit.getId(), benefitId)
                                && benefit.getServerId().equals(serverId)).findAny();

        if (benefits.isEmpty()) {
            throw new InternalException("", transactionId);
        }

        SubscriptionBenefitDto benefitModel = benefits.get();

        if (!benefitModel.getReactivable()) {
            if (obtainSubscriptionBenefit.findByUserIdAndBenefitIdAndServerId(userId, benefitModel.getId(), serverId).isPresent()) {
                throw new InternalException("benefit has been claimed", transactionId);
            }
            SubscriptionBenefitEntity subscriptionBenefit = new SubscriptionBenefitEntity();
            subscriptionBenefit.setBenefitId(benefitModel.getId());
            subscriptionBenefit.setCreatedAt(new Date());
            subscriptionBenefit.setUserId(userId);
            subscriptionBenefit.setServerId(benefitModel.getServerId());
            saveSubscriptionBenefit.save(subscriptionBenefit);
        }

        List<ItemQuantityModel> items = new ArrayList<>();
        if (benefitModel.getSendItem() && !benefitModel.getItems().isEmpty()) {
            items = benefitModel.getItems().stream().map(benefit -> new ItemQuantityModel(benefit.code,
                    benefit.quantity)).toList();
        }


        wowLibrePort.sendBenefitsPremium(jwt, serverId, userId, accountId, characterId, items, benefitModel.getType(),
                benefitModel.getAmount(),
                transactionId);

    }


}
