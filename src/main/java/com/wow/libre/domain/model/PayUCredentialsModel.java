package com.wow.libre.domain.model;

public record PayUCredentialsModel(String accountId, String merchantId, String signature, String test) {
}
