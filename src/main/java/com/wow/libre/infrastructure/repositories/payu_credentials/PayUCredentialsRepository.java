package com.wow.libre.infrastructure.repositories.payu_credentials;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface PayUCredentialsRepository extends CrudRepository<PayuCredentialsEntity, Long> {
    Optional<PayuCredentialsEntity> findByGatewayId(Long gatewayId);
}
