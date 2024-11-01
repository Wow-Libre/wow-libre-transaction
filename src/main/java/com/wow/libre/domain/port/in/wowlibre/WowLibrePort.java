package com.wow.libre.domain.port.in.wowlibre;

import com.wow.libre.domain.model.*;

import java.util.*;

public interface WowLibrePort {
    String getJwt(String transactionId);

    void sendPurchases(String jwt, Long serverId, Long userId, Long accountId, List<ItemQuantityModel> items,
                       String reference,
                       String transactionId);
}
