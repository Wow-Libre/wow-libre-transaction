package com.wow.libre.infrastructure.repositories.product;

import com.wow.libre.domain.port.out.product.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaProductAdapter implements ObtainProducts, SaveProducts {
    private final ProductRepository productRepository;

    public JpaProductAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductEntity> findByStatusIsTrueAndLanguage(String language, String transactionId) {
        return productRepository.findByStatusIsTrueAndLanguage(language);
    }

    @Override
    public List<ProductEntity> findByStatusIsTrueAndDiscount(String language, String transactionId) {
        return productRepository.findByStatusIsTrueAndDiscount(language);
    }

    @Override
    public Optional<ProductEntity> findByReferenceNumber(String referenceNumber, String transactionId) {
        return productRepository.findByReferenceNumberAndStatusIsTrue(referenceNumber);
    }

    @Override
    public Optional<ProductEntity> findByNameAndLanguage(String name, String language, String transactionId) {
        return productRepository.findByNameAndLanguage(name, language);
    }

    @Override
    public List<ProductEntity> findAllByStatusIsTrue(String transactionId) {
        return productRepository.findByStatusIsTrue();
    }

    @Override
    public Optional<ProductEntity> findById(Long productId, String transactionId) {
        return productRepository.findById(productId);
    }

    @Override
    public void save(ProductEntity product, String transactionId) {
        productRepository.save(product);
    }
}
