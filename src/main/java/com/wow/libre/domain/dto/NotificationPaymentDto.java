package com.wow.libre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class NotificationPaymentDto {
    @NotNull
    @JsonProperty("payment_id")
    private String paymentId;
}
