package com.wow.libre.domain.port.out.product_details;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainProductDetails {
    List<ProductDetailsEntity> findByProductId(ProductEntity product);
}
