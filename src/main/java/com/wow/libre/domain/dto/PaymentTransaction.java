package com.wow.libre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Builder
@Data
public class PaymentTransaction {
    @JsonProperty("date")
    private String date;

    @JsonProperty("payment_method_type")
    private int paymentMethodType;

    @JsonProperty("franchise")
    private String franchise;

    @JsonProperty("sign")
    private String sign;

    @JsonProperty("operation_date")
    private String operationDate;

    @JsonProperty("payment_request_state")
    private String paymentRequestState;

    @JsonProperty("bank_id")
    private String bankId;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("transaction_date")
    private String transactionDate;

    @JsonProperty("exchange_rate")
    private double exchangeRate;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("reference_pol")
    private String referencePol;

    @JsonProperty("cc_holder")
    private String ccHolder;

    @JsonProperty("tax")
    private double tax;

    @JsonProperty("transaction_type")
    private String transactionType;

    @JsonProperty("state_pol")
    private int statePol;

    @JsonProperty("shipping_country")
    private String shippingCountry;

    @JsonProperty("billing_country")
    private String billingCountry;

    @JsonProperty("authorization_code")
    private String authorizationCode;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("cc_number")
    private String ccNumber;

    @JsonProperty("installments_number")
    private int installmentsNumber;

    @JsonProperty("value")
    private double value;

    @JsonProperty("payment_method_name")
    private String paymentMethodName;

    @JsonProperty("email_buyer")
    private String emailBuyer;

    @JsonProperty("response_message_pol")
    private String responseMessagePol;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("reference_sale")
    private String referenceSale;
}
