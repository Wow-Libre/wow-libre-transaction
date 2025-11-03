package com.wow.libre.infrastructure.schedule;

import com.wow.libre.domain.enums.SubscriptionStatus;
import com.wow.libre.domain.enums.TransactionStatus;
import com.wow.libre.domain.exception.InternalException;
import com.wow.libre.domain.model.ItemQuantityModel;
import com.wow.libre.domain.port.in.packages.PackagesPort;
import com.wow.libre.domain.port.in.subscription.SubscriptionPort;
import com.wow.libre.domain.port.in.wowlibre.WowLibrePort;
import com.wow.libre.domain.port.out.plan.ObtainPlan;
import com.wow.libre.domain.port.out.transaction.ObtainTransaction;
import com.wow.libre.domain.port.out.transaction.SaveTransaction;
import com.wow.libre.infrastructure.entities.SubscriptionEntity;
import com.wow.libre.infrastructure.entities.TransactionEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

            Long planId = Long.valueOf(transaction.getReferenceNumber());
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
