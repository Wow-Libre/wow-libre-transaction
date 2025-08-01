package com.wow.libre.domain.port.out.partner;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainPartner {
    Optional<PartnerEntity> getByRealmId(Long realmId, String transactionId);
}
