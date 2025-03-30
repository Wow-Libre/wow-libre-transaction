package com.wow.libre.domain.port.in.payment;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;

public interface PaymentPort {

    void processPayment(PaymentTransaction paymentTransaction, String transactionId);

    CreatePaymentRedirectDto createPayment(Long userId, String email, CreatePaymentDto createPaymentDto,
                                                String transactionId);

}
