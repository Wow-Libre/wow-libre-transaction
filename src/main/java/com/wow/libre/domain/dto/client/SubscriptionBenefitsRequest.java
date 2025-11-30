package com.wow.libre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import com.wow.libre.domain.model.*;
import lombok.*;

import java.util.*;

@JsonPropertyOrder({
        "realm_id",
        "user_id",
        "account_id",
        "character_id",
        "items",
        "benefit_type",
        "amount"
})
@Data
@AllArgsConstructor
public class SubscriptionBenefitsRequest {
    @JsonProperty("realm_id")
    private Long realmId;
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
