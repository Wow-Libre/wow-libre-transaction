package com.wow.libre.domain.dto;

import com.wow.libre.domain.enums.*;

import java.time.*;

public record PaymentMethodsDto(Long id, PaymentType paymentType, String name, LocalDateTime createdAt) {
}
