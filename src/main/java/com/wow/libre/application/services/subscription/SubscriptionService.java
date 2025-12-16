package com.wow.libre.application.services.subscription;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.benefit_premium.*;
import com.wow.libre.domain.port.in.subscription.*;
import com.wow.libre.domain.port.in.wowlibre.*;
import com.wow.libre.domain.port.out.plan.*;
import com.wow.libre.domain.port.out.resources.*;
import com.wow.libre.domain.port.out.subscription.*;
import com.wow.libre.domain.port.out.subscription_benefit.*;
import com.wow.libre.infrastructure.entities.*;
import com.wow.libre.infrastructure.util.*;
import jakarta.transaction.*;
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
    private final BenefitPremiumPort benefitPremiumPort;

    public SubscriptionService(ObtainSubscription obtainSubscription, ObtainJsonLoader obtainJsonLoader,
                               ObtainSubscriptionBenefit obtainSubscriptionBenefit
            , SaveSubscriptionBenefit saveSubscriptionBenefit
            , WowLibrePort wowLibrePort, SaveSubscription saveSubscription, ObtainPlan obtainPlan,
                               @Qualifier("product-reference") RandomString randomString,
                               BenefitPremiumPort benefitPremiumPort) {
        this.obtainSubscription = obtainSubscription;
        this.obtainJsonLoader = obtainJsonLoader;
        this.obtainSubscriptionBenefit = obtainSubscriptionBenefit;
        this.saveSubscriptionBenefit = saveSubscriptionBenefit;
        this.wowLibrePort = wowLibrePort;
        this.saveSubscription = saveSubscription;
        this.obtainPlan = obtainPlan;
        this.randomString = randomString;
        this.benefitPremiumPort = benefitPremiumPort;
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
    public SubscriptionEntity createSubscription(Long userId, Long planId, String transactionId) {

        Optional<SubscriptionEntity> subscriptionEntity = obtainSubscription.findByUserIdAndStatus(userId,
                SubscriptionStatus.ACTIVE.getType());

        if (subscriptionEntity.isPresent()) {
            throw new InternalException("You already have an active subscription", transactionId);
        }

        Optional<PlanEntity> plan = obtainPlan.findById(planId, transactionId);

        if (plan.isEmpty()) {
            LOGGER.error("Plan not found: planId {} UserId {} TransactionId {}", planId, userId, transactionId);
            throw new InternalException("Plan not found", transactionId);
        }

        PlanEntity planEntity = plan.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextInvoiceDate;
        if (planEntity.getFrequencyType().equalsIgnoreCase("YEARLY")) {
            nextInvoiceDate = now.plusYears(planEntity.getFrequencyValue());
        } else {
            nextInvoiceDate = now.plusMonths(planEntity.getFrequencyValue());
        }
        
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setUserId(userId);
        subscription.setPlanId(planEntity);
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
    public SubscriptionBenefitsDto benefits(Long userId, Long realmId, String language, String transactionId) {

        if (!isActiveSubscription(userId, transactionId)) {
            return new SubscriptionBenefitsDto(new ArrayList<>(), 0);
        }

        List<BenefitsPremiumDto> benefits =
                benefitPremiumPort.findByLanguageAndRealmId(language, realmId, transactionId).stream()
                        .filter(benefit -> realmId.equals(benefit.realmId) && (benefit.reactivable
                                || obtainSubscriptionBenefit.findByUserIdAndBenefitIdAndServerId(userId,
                                benefit.id, realmId).isEmpty())).toList();

        return new SubscriptionBenefitsDto(benefits, benefits.size());
    }

    @Transactional
    @Override
    public void claimBenefits(Long realmId, Long userId, Long accountId, Long characterId, String language,
                              Long benefitId, String transactionId) {

        if (!isActiveSubscription(userId, transactionId)) {
            throw new InternalException("Subscription Inactive", transactionId);
        }

        Optional<BenefitsPremiumDto> benefits =
                benefitPremiumPort.findByLanguageAndRealmId(language, realmId, transactionId).stream()
                        .filter(benefit -> Objects.equals(benefit.id, benefitId)
                                && benefit.realmId.equals(realmId)).findAny();

        if (benefits.isEmpty()) {
            throw new InternalException("Benefits not available", transactionId);
        }

        BenefitsPremiumDto benefitModel = benefits.get();

        if (!benefitModel.reactivable) {
            if (obtainSubscriptionBenefit.findByUserIdAndBenefitIdAndServerId(userId, benefitModel.id, realmId).isPresent()) {
                throw new InternalException("benefit has been claimed", transactionId);
            }
            SubscriptionBenefitEntity subscriptionBenefit = new SubscriptionBenefitEntity();
            subscriptionBenefit.setBenefitId(benefitModel.id);
            subscriptionBenefit.setCreatedAt(new Date());
            subscriptionBenefit.setUserId(userId);
            subscriptionBenefit.setRealmId(benefitModel.realmId);
            saveSubscriptionBenefit.save(subscriptionBenefit);
        }

        List<ItemQuantityModel> items = new ArrayList<>();
        if (benefitModel.sendItem && !benefitModel.items.isEmpty()) {
            items = benefitModel.items.stream().map(benefit -> new ItemQuantityModel(benefit.getCode(),
                    benefit.getQuantity())).toList();
        }


        wowLibrePort.sendBenefitsPremium(realmId, userId, accountId, characterId, items, benefitModel.type.getType(),
                benefitModel.amount, transactionId);
    }

    @Override
    public List<SubscriptionEntity> findByExpirateSubscription() {
        return obtainSubscription.findByExpirateSubscription();
    }

    @Override
    public void save(SubscriptionEntity subscription) {
        saveSubscription.save(subscription, "");
    }


}
