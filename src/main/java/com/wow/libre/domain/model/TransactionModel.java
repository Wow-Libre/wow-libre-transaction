package com.wow.libre.domain.model;

import com.wow.libre.domain.enums.*;
import lombok.*;

@Getter
@Builder
public class TransactionModel {
    private Long userId;
    private Long accountId;
    private Long realmId;
    private boolean isSubscription;
    private String productReference;
    private PaymentType paymentType;
}
