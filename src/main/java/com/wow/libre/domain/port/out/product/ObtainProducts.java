package com.wow.libre.domain.port.out.product;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainProducts {
    List<ProductEntity> findByStatusIsTrue(String transactionId);

    Optional<ProductEntity> findByReferenceNumber(String referenceCode, String transactionId);
}
