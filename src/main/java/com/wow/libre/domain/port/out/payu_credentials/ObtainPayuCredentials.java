package com.wow.libre.domain.port.out.payu_credentials;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainPayuCredentials {
    Optional<PayuCredentialsEntity> findByPayUCredentials(Long gatewayId, String transactionId);
}
