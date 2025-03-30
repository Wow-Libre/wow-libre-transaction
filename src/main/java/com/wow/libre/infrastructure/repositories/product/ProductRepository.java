package com.wow.libre.infrastructure.repositories.product;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;


public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    List<ProductEntity> findByStatusIsTrueAndLanguage(String language);

    Optional<ProductEntity> findByReferenceNumberAndStatusIsTrue(String referenceNumber);

    @Query("select p FROM ProductEntity p WHERE p.status=true AND p.discount>0 AND p.language =:language")
    List<ProductEntity> findByStatusIsTrueAndDiscount(@Param("language") String language);
}
