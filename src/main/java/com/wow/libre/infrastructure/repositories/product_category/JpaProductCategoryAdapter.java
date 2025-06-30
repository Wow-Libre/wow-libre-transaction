package com.wow.libre.infrastructure.repositories.product_category;

import com.wow.libre.domain.port.out.product_category.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaProductCategoryAdapter implements ObtainProductCategory, SaveProductCategory {
    private final ProductCategoryRepository productCategoryRepository;

    public JpaProductCategoryAdapter(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public List<ProductCategoryEntity> findAll() {
        return productCategoryRepository.findAll();
    }

    @Override
    public Optional<ProductCategoryEntity> findByName(String name) {
        return productCategoryRepository.findByName(name);
    }

    @Override
    public Optional<ProductCategoryEntity> findById(Long id, String transactionId) {
        return productCategoryRepository.findById(id);
    }

    @Override
    public void save(ProductCategoryEntity productCategory, String transactionId) {
        productCategoryRepository.save(productCategory);
    }
}
