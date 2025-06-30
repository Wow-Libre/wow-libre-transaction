package com.wow.libre.domain.port.in.partner;

import com.wow.libre.infrastructure.entities.*;

public interface PartnerPort {
    void create(String name, Long realmId, String transactionId);

    boolean exists(Long realmId, String transactionId);

    PartnerEntity getByRealmId(Long realmId, String transactionId);

}
