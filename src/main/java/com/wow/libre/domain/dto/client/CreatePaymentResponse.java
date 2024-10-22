package com.wow.libre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class CreatePaymentResponse {
    private String id;
    private Double amount;
    private String currency;
    private String country;
    private String description;
    private String createdDate;
    private String status;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("notification_url")
    private String notificationsUrl;
    @JsonProperty("success_url")
    private String successUrl;
    @JsonProperty("back_url")
    private String backUrl;
    @JsonProperty("redirect_url")
    private String redirectUrl;
    @JsonProperty("merchant_checkout_token")
    private String merchantCheckoutToken;
    private Boolean direct;
}
