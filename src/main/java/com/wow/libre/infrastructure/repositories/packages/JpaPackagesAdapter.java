package com.wow.libre.infrastructure.repositories.packages;


import com.wow.libre.domain.port.out.packages.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaPackagesAdapter implements ObtainPackages, SavePackages {
    private final PackagesRepository packagesRepository;

    public JpaPackagesAdapter(PackagesRepository packagesRepository) {
        this.packagesRepository = packagesRepository;
    }

    @Override
    public List<PackagesEntity> findByProductId(ProductEntity product, String transactionId) {
        return packagesRepository.findByProductId(product);
    }

    @Override
    public void save(PackagesEntity packageEntity, String transactionId) {
        packagesRepository.save(packageEntity);
    }

}
