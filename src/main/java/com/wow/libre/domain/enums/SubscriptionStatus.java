package com.wow.libre.domain.enums;

import lombok.*;

import java.util.*;

@Getter
@AllArgsConstructor
public enum SubscriptionStatus {
    ACTIVE("CREATED", 100),
    PENDING("PENDING", 50);

    private final String type;
    private final int status;

    public static SubscriptionStatus getType(String type) {
        return Arrays.stream(values())
                .filter(transaction -> transaction.type.equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);
    }
}
