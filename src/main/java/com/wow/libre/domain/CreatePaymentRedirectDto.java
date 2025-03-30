package com.wow.libre.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRedirectDto {
    public String redirect;
    public String confirmationUrl;
    public String responseUrl;
    public String buyerEmail;
    public String signature;
    public String currency;
    public String taxReturnBase;
    public String tax;
    public String amount;
    public String referenceCode;
    public String description;
    public String accountId;
    public String merchantId;
    public String test;
    public boolean isPayment;

    public CreatePaymentRedirectDto(boolean isPayment, String redirect) {
        this.isPayment = isPayment;
        this.redirect = redirect;
    }
}
