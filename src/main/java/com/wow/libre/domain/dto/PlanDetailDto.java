package com.wow.libre.domain.dto;

import lombok.*;

@AllArgsConstructor
@Data
public class PlanDetailDto {
    private String name;
    private Double price;
    private Double discountedPrice;
    private Integer discount;
    private boolean status;
}
