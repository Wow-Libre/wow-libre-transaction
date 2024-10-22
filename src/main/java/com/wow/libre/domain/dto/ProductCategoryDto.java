package com.wow.libre.domain.dto;

import lombok.*;

import java.util.*;


@AllArgsConstructor
public class ProductCategoryDto {
    public Long id;
    public String name;
    public String description;
    public String disclaimer;
    public List<ProductsDto> products;
}
