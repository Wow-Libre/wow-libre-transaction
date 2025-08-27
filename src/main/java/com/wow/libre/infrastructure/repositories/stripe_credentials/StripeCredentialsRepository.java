package com.wow.libre.infrastructure.repositories.stripe_credentials;

import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface StripeCredentialsRepository extends CrudRepository<StripeCredentialsEntity, Long> {
    Optional<StripeCredentialsEntity> findByGatewayId(Long gatewayId);
}
