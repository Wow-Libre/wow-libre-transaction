package com.wow.libre.domain.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
public class CreatePaymentDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long serverId;
    @NotNull
    private Long accountId;
    @NotNull
    private Boolean isSubscription;
    @NotNull
    @NotEmpty
    private String referenceNumber;
}
