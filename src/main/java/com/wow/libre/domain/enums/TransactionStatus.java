package com.wow.libre.domain.enums;

import lombok.*;

import java.util.*;

@Getter
@AllArgsConstructor
public enum TransactionStatus {
    PAID("PAID", 100),
    CREATED("CREATED", 50),
    DELIVERED("DELIVERED", 100),
    PENDING("PENDING", 50);

    private final String type;
    private final int status;

    public static TransactionStatus getType(String type) {
        return Arrays.stream(values())
                .filter(transaction -> transaction.type.equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);
    }
}
