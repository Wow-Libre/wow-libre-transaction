package com.wow.libre.domain.port.out.stripe_credentials;

import com.wow.libre.infrastructure.entities.*;

public interface SaveStripeCredentials {
    void save(StripeCredentialsEntity stripeCredentials, String transactionId);

    void delete(StripeCredentialsEntity stripeCredentials, String transactionId);
}
