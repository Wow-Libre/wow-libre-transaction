package com.wow.libre.domain.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class ProductCategoryDto {
    private Long id;
    private String name;
    private String description;
    private String disclaimer;
}
