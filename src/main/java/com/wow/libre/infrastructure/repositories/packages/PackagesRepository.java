package com.wow.libre.infrastructure.repositories.packages;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface PackagesRepository extends CrudRepository<PackagesEntity, Long> {
    List<PackagesEntity> findByProductId(ProductEntity product);
}
