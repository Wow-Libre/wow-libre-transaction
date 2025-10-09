package com.wow.libre.domain.port.in.product;

import com.wow.libre.domain.dto.*;
import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ProductPort {
    Map<String, List<ProductCategoryModel>> products(String language, String transactionId);

    ProductDto product(String referenceCode, String transactionId);

    ProductEntity getProduct(String referenceCode, String transactionId);

    List<ProductDiscountsDto> productDiscounts(String language, String transactionId);

    void createProduct(CreateProductDto product, String transactionId);

    ProductsDetailsDto allProducts(String transactionId);

    void deleteProduct(Long productId, String transactionId);
}
