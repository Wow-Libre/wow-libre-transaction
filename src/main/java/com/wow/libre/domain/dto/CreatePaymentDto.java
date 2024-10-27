package com.wow.libre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
public class CreatePaymentDto {

    @NotNull
    @JsonProperty("account_id")
    private Long accountId;
    @NotNull
    @JsonProperty("is_subscription")
    private Boolean isSubscription;
    @NotNull
    @JsonProperty("reference_number")
    private String referenceNumber;
}
