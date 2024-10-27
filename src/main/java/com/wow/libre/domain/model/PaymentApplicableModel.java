package com.wow.libre.domain.model;

import lombok.*;


public record PaymentApplicableModel(boolean isPayment, Double amount, String currency, String orderId,
                                     String description) {
}
