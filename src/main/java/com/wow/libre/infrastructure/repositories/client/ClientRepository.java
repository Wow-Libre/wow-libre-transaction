package com.wow.libre.infrastructure.repositories.client;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByUsername(String username);
}
