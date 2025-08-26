package com.wow.libre.domain.port.out.payment_gateway;

import com.wow.libre.domain.enums.*;
import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainPaymentGateway {
    Optional<PaymentGatewaysEntity> findByPaymentType(PaymentType paymentType, String transactionId);

    List<PaymentGatewaysEntity> findByIsActiveIsTrue(String transactionId);

    Optional<PaymentGatewaysEntity> findByPaymentTypeId(Long paymentTypeId, String transactionId);
}
