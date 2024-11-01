package com.wow.libre.domain.enums;

import lombok.*;

@Getter
@AllArgsConstructor
public enum TransactionStatus {
    PAID("PAID");

    private final String type;
}
