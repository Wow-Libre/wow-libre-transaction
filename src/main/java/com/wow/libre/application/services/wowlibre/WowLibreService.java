package com.wow.libre.application.services.wowlibre;

import com.wow.libre.domain.dto.client.SubscriptionBenefitsRequest;
import com.wow.libre.domain.model.ItemQuantityModel;
import com.wow.libre.domain.port.in.wowlibre.WowLibrePort;
import com.wow.libre.infrastructure.client.WowLibreClient;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WowLibreService implements WowLibrePort {
  private final WowLibreClient wowLibreClient;

  public WowLibreService(WowLibreClient wowLibreClient) {
    this.wowLibreClient = wowLibreClient;
  }


  @Override
  public void sendPurchases(Long realmId, Long userId, Long accountId, Double gold,
                            List<ItemQuantityModel> items, String reference, String transactionId) {
    wowLibreClient.sendPurchases(realmId, userId, accountId, gold, items, reference, transactionId);
  }

  @Override
  public void sendBenefitsPremium(Long realmId, Long userId, Long accountId,
                                  Long characterId,
                                  List<ItemQuantityModel> items, String benefitType, Double amount,
                                  String transactionId) {
    wowLibreClient.sendBenefitsPremium(new SubscriptionBenefitsRequest(realmId, userId, accountId,
        characterId, items, benefitType, amount), transactionId);
  }
}
