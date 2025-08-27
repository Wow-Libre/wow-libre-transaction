package com.wow.libre.domain.port.out.payment_gateway;

import com.wow.libre.infrastructure.entities.*;

public interface SavePaymentGateway {
    void save(PaymentGatewaysEntity paymentGateways, String transactionId);

    void delete(PaymentGatewaysEntity paymentGateways, String transactionId);
}
