package com.wow.libre.application.services.wowlibre;

import com.wow.libre.domain.dto.client.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.wowlibre.*;
import com.wow.libre.infrastructure.client.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class WowLibreService implements WowLibrePort {
    private final WowLibreClient wowLibreClient;

    public WowLibreService(WowLibreClient wowLibreClient) {
        this.wowLibreClient = wowLibreClient;
    }


    @Override
    public void sendPurchases(Long serverId, Long userId, Long accountId, Double gold,
                              List<ItemQuantityModel> items, String reference, String transactionId) {
        wowLibreClient.sendPurchases(serverId, userId, accountId, gold, items, reference, transactionId);
    }

    @Override
    public void sendBenefitsPremium(String jwt, Long serverId, Long userId, Long accountId,
                                    Long characterId,
                                    List<ItemQuantityModel> items, String benefitType, Double amount,
                                    String transactionId) {
        wowLibreClient.sendBenefitsPremium(jwt, new SubscriptionBenefitsRequest(serverId, userId, accountId,
                        characterId, items, benefitType, amount),
                transactionId);
    }
}
