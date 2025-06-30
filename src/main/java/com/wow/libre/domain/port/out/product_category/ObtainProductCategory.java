package com.wow.libre.domain.port.out.product_category;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainProductCategory {
    List<ProductCategoryEntity> findAll();

    Optional<ProductCategoryEntity> findByName(String name);

    Optional<ProductCategoryEntity> findById(Long id, String transactionId);

}
