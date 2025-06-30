package com.wow.libre.domain.port.out.product;

import com.wow.libre.infrastructure.entities.*;

public interface SaveProducts {
    void save(ProductEntity product, String transactionId);
}
