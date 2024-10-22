package com.wow.libre.domain.port.in.payment;

import com.wow.libre.domain.dto.*;

public interface PaymentPort {
    String createPayment(CreatePaymentDto createPaymentDto, String transactionId);
}
