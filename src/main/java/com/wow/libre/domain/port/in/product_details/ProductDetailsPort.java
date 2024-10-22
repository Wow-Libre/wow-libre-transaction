package com.wow.libre.domain.port.in.product_details;

import com.wow.libre.domain.model.*;
import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ProductDetailsPort {
    List<ProductDetailModel> findByProductId(ProductEntity product, String transactionId);
}
