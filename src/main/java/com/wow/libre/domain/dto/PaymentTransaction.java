package com.wow.libre.domain.dto;

import lombok.*;

@Builder
@Data
public class PaymentTransaction {
    private String date;
    private int paymentMethodType;
    private String franchise;
    private String sign;
    private String operationDate;
    private String paymentRequestState;
    private String bankId;
    private String paymentMethod;
    private String transactionId;
    private String transactionDate;
    private double exchangeRate;
    private String ip;
    private String referencePol;
    private String ccHolder;
    private double tax;
    private String transactionType;
    private String shippingCountry;
    private String billingCountry;
    private String authorizationCode;
    private String currency;
    private String ccNumber;
    private int installmentsNumber;
    private String value;
    private String paymentMethodName;
    private String emailBuyer;
    private String responseMessagePol;
    private String accountId;
    private String referenceSale;
    private String merchantId;
    private String statePol;
}
