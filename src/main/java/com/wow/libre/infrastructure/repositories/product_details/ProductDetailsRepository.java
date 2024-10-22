package com.wow.libre.infrastructure.repositories.product_details;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ProductDetailsRepository extends CrudRepository<ProductDetailsEntity, Long> {
    List<ProductDetailsEntity> findByProductId(ProductEntity product);

}
