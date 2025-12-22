package com.wow.libre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import com.wow.libre.domain.model.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@JsonPropertyOrder({
        "realm_id",
        "user_id",
        "account_id",
        "reference",
        "items",
        "amount"
})
@AllArgsConstructor
@Getter
public class CreateTransactionItemsDto {
    @JsonProperty("realm_id")
    private Long realmId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("account_id")
    private Long accountId;
    private String reference;
    @NotNull
    private List<ItemQuantityModel> items;
    private Double amount;
}
