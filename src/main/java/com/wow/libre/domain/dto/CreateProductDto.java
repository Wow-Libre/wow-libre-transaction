package com.wow.libre.domain.dto;

import lombok.*;

import java.util.*;

@Data
public class CreateProductDto {
    private String name;
    private Long productCategoryId;
    private String disclaimer;
    private Double price;
    private Integer discount;
    private String description;
    private String imageUrl;
    private Long realmId;
    private String language;
    private String tax;
    private String returnTax;
    private Long creditPointsValue;
    private boolean creditPointsEnabled;
    private List<ProductDetailsDto> details;
    private List<String> packages;
}
