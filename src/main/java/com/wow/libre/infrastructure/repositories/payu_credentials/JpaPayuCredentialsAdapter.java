package com.wow.libre.infrastructure.repositories.payu_credentials;

import com.wow.libre.domain.port.out.payu_credentials.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaPayuCredentialsAdapter implements ObtainPayuCredentials, SavePayUCredentials {

    private final PayUCredentialsRepository payUCredentialsRepository;

    public JpaPayuCredentialsAdapter(PayUCredentialsRepository payUCredentialsRepository) {
        this.payUCredentialsRepository = payUCredentialsRepository;
    }

    @Override
    public Optional<PayuCredentialsEntity> findByPayUCredentials(Long gatewayId, String transactionId) {
        return payUCredentialsRepository.findByGatewayId(gatewayId);
    }

    @Override
    public void save(PayuCredentialsEntity payuCredentialsEntity, String transactionId) {
        payUCredentialsRepository.save(payuCredentialsEntity);
    }

    @Override
    public void delete(PayuCredentialsEntity payuCredentials, String transactionId) {
        payUCredentialsRepository.delete(payuCredentials);
    }
}
