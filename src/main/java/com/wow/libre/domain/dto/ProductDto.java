package com.wow.libre.domain.dto;

import com.wow.libre.domain.model.*;
import lombok.*;

import java.util.*;

@Getter
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String disclaimer;
    private String category;
    private Double price;
    private Integer discount;
    private boolean usePoints;
    private String description;
    private String imgUrl;
    private String partner;
    private Long serverId;
    private String referenceNumber;
    private List<ProductDetailModel> details;
}
