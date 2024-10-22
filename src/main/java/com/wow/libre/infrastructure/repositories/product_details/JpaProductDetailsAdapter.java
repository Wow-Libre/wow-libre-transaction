package com.wow.libre.infrastructure.repositories.product_details;

import com.wow.libre.domain.port.out.product_details.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaProductDetailsAdapter implements ObtainProductDetails {
    private final ProductDetailsRepository productDetailsRepository;

    public JpaProductDetailsAdapter(ProductDetailsRepository productDetailsRepository) {
        this.productDetailsRepository = productDetailsRepository;
    }

    @Override
    public List<ProductDetailsEntity> findByProductId(ProductEntity product) {
        return productDetailsRepository.findByProductId(product);
    }
}
