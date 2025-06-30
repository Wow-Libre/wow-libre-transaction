package com.wow.libre.application.services.partner;

import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.port.in.partner.*;
import com.wow.libre.domain.port.out.partner.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

@Service
public class PartnerService implements PartnerPort {
    private final ObtainPartner obtainPartner;
    private final SavePartner savePartner;

    public PartnerService(ObtainPartner obtainPartner, SavePartner savePartner) {
        this.obtainPartner = obtainPartner;
        this.savePartner = savePartner;
    }

    @Override
    public void create(String name, Long realmId, String transactionId) {

        obtainPartner.getByRealmId(realmId, transactionId)
                .ifPresent(partner -> {
                    throw new InternalException("Partner already exists for realmId: " + realmId, transactionId);
                });

        PartnerEntity partner = new PartnerEntity();
        partner.setName(name);
        partner.setRealmId(realmId);
        partner.setStatus(true);
        savePartner.save(partner, transactionId);
    }

    @Override
    public boolean exists(Long realmId, String transactionId) {
        return obtainPartner.getByRealmId(realmId, transactionId).isPresent();
    }

    @Override
    public PartnerEntity getByRealmId(Long realmId, String transactionId) {
        return obtainPartner.getByRealmId(realmId, transactionId).orElseThrow(
                () -> new NotFoundException("Partner not found for realmId: " + realmId, transactionId)
        );
    }
}
