package com.wow.libre.infrastructure.repositories.partner;

import com.wow.libre.domain.port.out.partner.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaPartnerAdapter implements ObtainPartner, SavePartner {

    private final PartnerRepository partnerRepository;

    public JpaPartnerAdapter(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    public Optional<PartnerEntity> getByRealmId(Long realmId, String transactionId) {
        return partnerRepository.findByRealmId(realmId);
    }

    @Override
    public void save(PartnerEntity partner, String transactionId) {
        partnerRepository.save(partner);
    }
}
