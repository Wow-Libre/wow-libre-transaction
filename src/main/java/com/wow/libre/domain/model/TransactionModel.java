package com.wow.libre.domain.model;

import lombok.*;

import java.time.*;

@Getter
@Builder
public class TransactionModel {
    private Long userId;
    private Long accountId;
    private Long serverId;
    private String amount;
    private boolean status;
    private String referenceNumber;
    private boolean isSubscription;
    private LocalDateTime creationDate;
}
