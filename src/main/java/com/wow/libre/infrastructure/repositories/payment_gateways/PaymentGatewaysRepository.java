package com.wow.libre.infrastructure.repositories.payment_gateways;

import com.wow.libre.domain.enums.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface PaymentGatewaysRepository extends CrudRepository<PaymentGatewaysEntity, Long> {
    Optional<PaymentGatewaysEntity> findByTypeAndIsActiveIsTrue(PaymentType type);

    List<PaymentGatewaysEntity> findByIsActiveIsTrue();
}
