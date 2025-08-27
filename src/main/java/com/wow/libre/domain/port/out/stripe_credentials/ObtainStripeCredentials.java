package com.wow.libre.domain.port.out.stripe_credentials;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainStripeCredentials {
    Optional<StripeCredentialsEntity> findByPayUCredentials(Long gatewayId, String transactionId);
}
