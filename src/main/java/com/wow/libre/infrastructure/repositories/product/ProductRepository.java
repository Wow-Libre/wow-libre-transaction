package com.wow.libre.infrastructure.repositories.product;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;


public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    List<ProductEntity> findByStatusIsTrue();

    Optional<ProductEntity> findByReferenceNumberAndStatusIsTrue(String referenceNumber);

    @Query("select p FROM ProductEntity p WHERE p.status=true AND p.discount>0")
    List<ProductEntity> findByStatusIsTrueAndDiscount();
}
