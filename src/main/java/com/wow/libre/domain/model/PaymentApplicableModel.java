package com.wow.libre.domain.model;

public record PaymentApplicableModel(boolean isPayment, Double amount, String currency, String orderId,
                                     String description, String tax,
                                     String returnTax) {
}
