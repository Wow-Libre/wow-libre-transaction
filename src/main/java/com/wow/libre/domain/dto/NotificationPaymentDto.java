package com.wow.libre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class NotificationPaymentDto {

    @JsonProperty("payment_id")
    private String paymentId;
}
