package com.wow.libre.infrastructure.schedule;

import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.packages.*;
import com.wow.libre.domain.port.in.subscription.*;
import com.wow.libre.domain.port.in.wowlibre.*;
import com.wow.libre.domain.port.out.plan.*;
import com.wow.libre.domain.port.out.transaction.*;
import com.wow.libre.infrastructure.entities.*;
import jakarta.transaction.*;
import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class TransactionSchedule {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionSchedule.class);

    private final ObtainTransaction obtainTransaction;
    private final SaveTransaction saveTransaction;
    private final WowLibrePort wowLibrePort;
    private final PackagesPort packagesPort;
    private final SubscriptionPort subscriptionPort;
    private final ObtainPlan obtainPlan;

    public TransactionSchedule(ObtainTransaction obtainTransaction, SaveTransaction saveTransaction,
                               WowLibrePort wowLibrePort,
                               PackagesPort packagesPort, SubscriptionPort subscriptionPort, ObtainPlan obtainPlan) {
        this.obtainTransaction = obtainTransaction;
        this.saveTransaction = saveTransaction;
        this.wowLibrePort = wowLibrePort;
        this.packagesPort = packagesPort;
        this.subscriptionPort = subscriptionPort;
        this.obtainPlan = obtainPlan;
    }

    @Transactional
    @Scheduled(cron = "1/50 * * * * *")
    public void sendPurchases() {
        final String transactionId = "Internal-SendPurchases";
        List<TransactionEntity> transactionEntities = obtainTransaction.findByStatusIsPaidAndSendIsFalse(transactionId);

        for (TransactionEntity transaction : transactionEntities) {
            try {

                if (transaction.isSubscription()) {

                    boolean activeSubscription = subscriptionPort.isActiveSubscription(transaction.getUserId(),
                            transactionId);

                    if (!activeSubscription) {
                        List<PlanEntity> activePlans = obtainPlan.findByStatusIsTrue(transactionId);
                        if (activePlans.isEmpty()) {
                            LOGGER.error("There is no active plan. TransactionId: {}", transactionId);
                            throw new InternalException("There is no active plan", transactionId);
                        }
                        Long planId = activePlans.get(0).getId();
                        subscriptionPort.createSubscription(transaction.getUserId(), planId, transactionId);
                        transaction.setStatus(TransactionStatus.DELIVERED.getType());
                        transaction.setSend(true);
                    }

                } else {
                    List<ItemQuantityModel> items = packagesPort.findByProductId(transaction.getProductId(),
                            transactionId);

                    double amount = transaction.isCreditPoints() ? transaction.getPrice() : 0d;

                    if (amount <= 0 && items.isEmpty()) {
                        throw new InternalException("No send transaction invalid", "");
                    }

                    wowLibrePort.sendPurchases(transaction.getRealmId(), transaction.getUserId(),
                            transaction.getAccountId(), amount, items, transaction.getReferenceNumber(), transactionId);
                    transaction.setSend(true);
                    transaction.setStatus(TransactionStatus.DELIVERED.getType());
                }
                saveTransaction.save(transaction, transactionId);
            } catch (Exception e) {
                LOGGER.error("Error Transaction Sends {}", e.getLocalizedMessage());
            }
        }

    }

    @Transactional
    @Scheduled(cron = "1/50 * * * * *")
    public void verifySubscriptions() {
        List<SubscriptionEntity> subscriptions = subscriptionPort.findByExpirateSubscription();

        subscriptions.forEach(subscription -> {
            subscription.setStatus(SubscriptionStatus.INACTIVE.getType());
            subscriptionPort.save(subscription);
        });

    }
}
