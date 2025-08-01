package com.wow.libre.infrastructure.repositories.product_category;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ProductCategoryRepository extends CrudRepository<ProductCategoryEntity, Long> {
    Optional<ProductCategoryEntity> findByName(String name);

    @Override
    List<ProductCategoryEntity> findAll();
}
