package com.wow.libre.domain.dto;

import com.wow.libre.domain.model.*;
import lombok.*;

import java.util.*;

@Builder
@Data
public class ProductsDetailsDto {
    private List<ProductModel> products;
    private Integer totalProducts;
}
