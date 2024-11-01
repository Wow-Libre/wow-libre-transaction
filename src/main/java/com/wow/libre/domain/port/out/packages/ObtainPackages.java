package com.wow.libre.domain.port.out.packages;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainPackages {
    List<PackagesEntity> findByProductId(ProductEntity product, String transactionId);
}
