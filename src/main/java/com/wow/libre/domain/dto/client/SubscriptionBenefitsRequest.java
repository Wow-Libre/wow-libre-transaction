package com.wow.libre.domain.dto.client;

import com.wow.libre.domain.model.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class SubscriptionBenefitsRequest {

    private Long serverId;

    private Long userId;

    private Long accountId;

    private Long characterId;
    private List<ItemQuantityModel> items;

    private String benefitType;
    private Double amount;
}
