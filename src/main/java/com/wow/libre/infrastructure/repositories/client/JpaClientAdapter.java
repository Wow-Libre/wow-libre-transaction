package com.wow.libre.infrastructure.repositories.client;

import com.wow.libre.domain.port.out.client.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaClientAdapter implements ObtainClient, SaveClient {
    private final ClientRepository clientRepository;

    public JpaClientAdapter(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<ClientEntity> findByUsername(String username, String transactionId) {
        return clientRepository.findByUsername(username);
    }

    @Override
    public void save(ClientEntity client, String transactionId) {
        clientRepository.save(client);
    }
}
