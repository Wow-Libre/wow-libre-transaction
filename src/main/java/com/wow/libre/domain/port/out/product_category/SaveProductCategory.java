package com.wow.libre.domain.port.out.product_category;

import com.wow.libre.infrastructure.entities.*;

public interface SaveProductCategory {
    void save(ProductCategoryEntity productCategory, String transactionId);
}
