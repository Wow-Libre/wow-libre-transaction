package com.wow.libre.domain.port.in.product_category;

import com.wow.libre.domain.dto.*;
import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ProductCategoryPort {
    List<ProductCategoryDto> findAllProductCategories(String transactionId);

    void createProductCategory(String name, String description, String disclaimer, String transactionId);

    ProductCategoryEntity findById(Long Id, String transactionId);

}
