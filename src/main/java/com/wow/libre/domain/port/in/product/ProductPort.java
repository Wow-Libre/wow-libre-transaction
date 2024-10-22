package com.wow.libre.domain.port.in.product;

import com.wow.libre.domain.dto.*;

import java.util.*;

public interface ProductPort {
    Map<String, List<ProductCategoryDto>> products(String transactionId);

    ProductDto product(String referenceCode, String transactionId);

}
