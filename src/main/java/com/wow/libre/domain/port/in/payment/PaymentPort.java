package com.wow.libre.domain.port.in.payment;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;

public interface PaymentPort {
    CreatePaymentRedirectDto createPayment(Long userId, CreatePaymentDto createPaymentDto, String transactionId);

    void processPayment(String orderId, String transactionId);
}
