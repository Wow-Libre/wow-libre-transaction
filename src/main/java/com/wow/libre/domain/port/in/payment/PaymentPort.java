package com.wow.libre.domain.port.in.payment;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;

public interface PaymentPort {

    void processPayment(PaymentTransaction paymentTransaction, PaymentType paymentType, String transactionId);

    CreatePaymentRedirectDto createPayment(Long userId, String email, CreatePaymentDto createPaymentDto,
                                           String transactionId);

}
