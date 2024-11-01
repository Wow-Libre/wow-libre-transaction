package com.wow.libre.domain.port.out.client;

import com.wow.libre.infrastructure.entities.*;

public interface SaveClient {
    void save(ClientEntity client, String transactionId);
}
