package com.wow.libre.application.services.packages;

import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.packages.*;
import com.wow.libre.domain.port.out.packages.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class PackagesService implements PackagesPort {
    private final ObtainPackages obtainPackages;

    public PackagesService(ObtainPackages obtainPackages) {
        this.obtainPackages = obtainPackages;
    }

    @Override
    public List<ItemQuantityModel> findByProductId(ProductEntity product, String transactionId) {
        return obtainPackages.findByProductId(product, transactionId).stream()
                .map(packages -> new ItemQuantityModel(packages.getCodeCore(), 1)).toList();
    }
}
