package com.wow.libre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@Builder
public class CreatePaymentRequest {
    private String currency;
    private Double amount;
    private String country;
    @JsonProperty("order_id")
    private String orderId;
    private String description;
    @JsonProperty("success_url")
    private String successUrl;
    @JsonProperty("back_url")
    private String backUrl;
    @JsonProperty("notification_url")
    private String notificationUrl;
}
