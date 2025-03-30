package com.wow.libre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class CreatePaymentDto {
    @JsonProperty("account_id")
    private Long accountId;
    @NotNull
    @JsonProperty("is_subscription")
    private Boolean isSubscription;
    @JsonProperty("server_id")
    private Long serverId;
    @JsonProperty("product_reference")
    private String productReference;
}
