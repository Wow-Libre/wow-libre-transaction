package com.wow.libre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import com.wow.libre.domain.model.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class SubscriptionBenefitsRequest {
    @JsonProperty("server_id")
    private Long serverId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("character_id")
    private Long characterId;
    private List<ItemQuantityModel> items;
    @JsonProperty("benefit_type")
    private String benefitType;
    private Double amount;
}
