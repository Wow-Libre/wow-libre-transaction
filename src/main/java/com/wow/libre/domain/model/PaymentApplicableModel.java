package com.wow.libre.domain.model;

import lombok.*;

@AllArgsConstructor
public record PaymentApplicableModel(boolean isPayment, Double amount, String currency, Long orderId) {
}
