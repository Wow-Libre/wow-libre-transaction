package com.wow.libre.domain.dto;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@Getter
public class SubscriptionBenefitsDto {
    private List<SubscriptionBenefitDto> benefits;
    private int size;
}
