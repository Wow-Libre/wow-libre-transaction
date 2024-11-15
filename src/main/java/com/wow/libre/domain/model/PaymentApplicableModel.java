package com.wow.libre.domain.model;


public class PaymentApplicableModel {
    public final boolean isPayment;
    public final Double amount;
    public final String currency;
    public final String orderId;
    public final String description;
    public String subscriptionUrl;

    public PaymentApplicableModel(boolean isPayment, Double amount, String currency, String orderId,
                                  String description) {
        this.isPayment = isPayment;
        this.amount = amount;
        this.currency = currency;
        this.orderId = orderId;
        this.description = description;
    }

    public PaymentApplicableModel(boolean isPayment, Double amount, String currency, String orderId,
                                  String description, String subscriptionUrl) {
        this.isPayment = isPayment;
        this.amount = amount;
        this.currency = currency;
        this.orderId = orderId;
        this.description = description;
        this.subscriptionUrl = subscriptionUrl;
    }
}
