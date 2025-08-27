package com.wow.libre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class CreatePaymentDto {
    @JsonProperty("account_id")
    @NotNull
    private Long accountId;
    @NotNull
    @JsonProperty("is_subscription")
    private Boolean isSubscription;
    @JsonProperty("realm_id")
    @NotNull
    private Long realmId;
    @JsonProperty("product_reference")
    @NotNull
    private String productReference;
    @JsonProperty("payment_type")
    @NotNull
    private String paymentType;
}
