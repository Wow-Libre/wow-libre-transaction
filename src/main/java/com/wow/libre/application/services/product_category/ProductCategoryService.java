package com.wow.libre.application.services.product_category;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.port.in.product_category.*;
import com.wow.libre.domain.port.out.product_category.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ProductCategoryService implements ProductCategoryPort {
    private final ObtainProductCategory obtainProductCategory;
    private final SaveProductCategory saveProductCategory;

    public ProductCategoryService(ObtainProductCategory obtainProductCategory,
                                  SaveProductCategory saveProductCategory) {
        this.obtainProductCategory = obtainProductCategory;
        this.saveProductCategory = saveProductCategory;
    }

    @Override
    public List<ProductCategoryDto> findAllProductCategories() {
        return obtainProductCategory.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public void createProductCategory(String name, String description, String disclaimer, String transactionId) {

        obtainProductCategory.findByName(name).ifPresent(productCategoryEntity -> {
            throw new InternalException(
                    String.format("Product category with name '%s' already exists.", name),
                    transactionId);
        });

        ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
        productCategoryEntity.setName(name);
        productCategoryEntity.setDescription(description);
        productCategoryEntity.setDisclaimer(disclaimer);
        saveProductCategory.save(productCategoryEntity, transactionId);
    }

    @Override
    public ProductCategoryEntity findById(Long id, String transactionId) {
        return obtainProductCategory.findById(id, transactionId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Product category with id '%s' not found. ", id
                        ), transactionId));
    }

    private ProductCategoryDto mapToDto(ProductCategoryEntity entity) {
        return new ProductCategoryDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDisclaimer()
        );
    }
}
