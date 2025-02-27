package com.wow.libre.domain.model;

import lombok.*;

@Getter
@Builder
public class TransactionModel {
    private Long userId;
    private Long accountId;
    private Long serverId;
    private boolean isSubscription;
    private String productReference;
}
