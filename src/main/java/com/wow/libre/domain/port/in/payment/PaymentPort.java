package com.wow.libre.domain.port.in.payment;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;

public interface PaymentPort {
    CreatePaymentRedirectDto createPayment(Long userId, CreatePaymentDto createPaymentDto, String transactionId);

    void processPayment(PaymentTransaction paymentTransaction, String transactionId);

    CreatePaymentRedirectDto createSubscription(Long userId, String email, CreatePaymentDto createPaymentDto,
                                                String transactionId);

}
