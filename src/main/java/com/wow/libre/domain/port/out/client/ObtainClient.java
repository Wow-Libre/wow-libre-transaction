package com.wow.libre.domain.port.out.client;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainClient {
    Optional<ClientEntity> findByUsername(String username, String transactionId);
}
