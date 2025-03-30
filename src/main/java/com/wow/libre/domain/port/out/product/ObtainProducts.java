package com.wow.libre.domain.port.out.product;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainProducts {
    List<ProductEntity> findByStatusIsTrueAndLanguage(String language, String transactionId);

    List<ProductEntity> findByStatusIsTrueAndDiscount(String language, String transactionId);

    Optional<ProductEntity> findByReferenceNumber(String referenceCode, String transactionId);
}
