package com.wow.libre.domain.port.out.packages;

import com.wow.libre.infrastructure.entities.*;

public interface SavePackages {
    void save(PackagesEntity packageEntity, String transactionId);
}

