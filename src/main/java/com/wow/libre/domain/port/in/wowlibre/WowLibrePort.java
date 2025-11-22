package com.wow.libre.domain.port.in.wowlibre;

import com.wow.libre.domain.model.ItemQuantityModel;
import java.util.List;

public interface WowLibrePort {

  void sendPurchases(Long realmId, Long userId, Long accountId, Double gold, List<ItemQuantityModel> items,
                     String reference, String transactionId);

  void sendBenefitsPremium(Long realmId, Long userId, Long accountId,
                           Long characterId, List<ItemQuantityModel> items,
                           String benefitType, Double amount, String transactionId);
}
