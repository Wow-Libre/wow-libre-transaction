package com.wow.libre.domain.dto;

import lombok.*;

@Getter
@Builder
public class ProductsDto {
    private Long id;
    private String name;
    private String disclaimer;
    private String category;
    private Double price;
    private Double discountedPrice;
    private Double discountedGoldPrice;
    private Integer discount;
    private boolean gamblingMoney;
    private Long goldPrice;
    private String description;
    private String imgUrl;
    private String partner;
    private String referenceNumber;
}
