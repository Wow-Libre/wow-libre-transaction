package com.wow.libre.application.services.payment;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.payment.*;
import com.wow.libre.domain.port.in.transaction.*;
import com.wow.libre.infrastructure.client.*;
import org.springframework.stereotype.*;

@Service
public class PaymentService implements PaymentPort {
    private final TransactionPort transactionPort;
    private final DLocalGoClient client;

    public PaymentService(TransactionPort transactionPort, DLocalGoClient client) {
        this.transactionPort = transactionPort;
        this.client = client;
    }

    @Override
    public String createPayment(CreatePaymentDto createPaymentDto, String transactionId) {
        return null;
    }
}
