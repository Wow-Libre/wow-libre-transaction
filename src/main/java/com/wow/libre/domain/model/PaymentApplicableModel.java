package com.wow.libre.domain.model;

import lombok.*;

@AllArgsConstructor
public class PaymentApplicableModel {
    public final boolean isPayment;
    public final Double amount;
    public final String currency;
    public final String orderId;
    public final String description;
    public final String tax;
    public final String returnTax;
}
