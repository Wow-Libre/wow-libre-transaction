package com.wow.libre.domain;

import lombok.*;

@AllArgsConstructor
public class CreatePaymentRedirectDto {
    public final String redirect;
    public final String confirmationUrl;
    public final String responseUrl;
    public final String buyerEmail;
    public final String signature;
    public final String currency;
    public final String taxReturnBase;
    public final String tax;
    public final String amount;
    public final String referenceCode;
    public final String description;
    public final String accountId;
    public final String merchantId;
    public final String test;
}
