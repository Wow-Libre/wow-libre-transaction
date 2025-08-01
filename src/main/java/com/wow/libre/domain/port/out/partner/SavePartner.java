package com.wow.libre.domain.port.out.partner;

import com.wow.libre.infrastructure.entities.*;

public interface SavePartner {
    void save(PartnerEntity partner, String transactionId);
}
