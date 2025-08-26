package com.wow.libre.domain.port.in.wowlibre;

import com.wow.libre.domain.model.*;

import java.util.*;

public interface WowLibrePort {

    void sendPurchases(String jwt, Long serverId, Long userId, Long accountId, Double gold,
                       List<ItemQuantityModel> items,
                       String reference,
                       String transactionId);

    void sendBenefitsPremium(String jwt, Long serverId, Long userId, Long accountId,
                             Long characterId, List<ItemQuantityModel> items,
                             String benefitType, Double amount, String transactionId);
}
