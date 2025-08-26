package com.wow.libre.infrastructure.repositories.stripe_credentials;

import com.wow.libre.domain.port.out.stripe_credentials.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaStripeCredentialsAdapter implements ObtainStripeCredentials, SaveStripeCredentials {
    private final StripeCredentialsRepository stripeCredentialsRepository;

    public JpaStripeCredentialsAdapter(StripeCredentialsRepository stripeCredentialsRepository) {
        this.stripeCredentialsRepository = stripeCredentialsRepository;
    }

    @Override
    public Optional<StripeCredentialsEntity> findByPayUCredentials(Long gatewayId, String transactionId) {
        return stripeCredentialsRepository.findByGatewayId(gatewayId);
    }

    @Override
    public void save(StripeCredentialsEntity stripeCredentials, String transactionId) {
        stripeCredentialsRepository.save(stripeCredentials);
    }

    @Override
    public void delete(StripeCredentialsEntity stripeCredentials, String transactionId) {
        stripeCredentialsRepository.delete(stripeCredentials);
    }
}
