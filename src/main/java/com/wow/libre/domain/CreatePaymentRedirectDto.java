package com.wow.libre.domain;


public record CreatePaymentRedirectDto(String redirect, String successUrl, String backUrl) {
}
