package com.wow.libre.domain.port.out.payu_credentials;

import com.wow.libre.infrastructure.entities.*;

public interface SavePayUCredentials {
    void save(PayuCredentialsEntity payuCredentialsEntity, String transactionId);

    void delete(PayuCredentialsEntity payuCredentials, String transactionId);
}
