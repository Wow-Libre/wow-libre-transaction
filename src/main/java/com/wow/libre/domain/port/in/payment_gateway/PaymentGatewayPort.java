package com.wow.libre.domain.port.in.payment_gateway;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.model.*;

import java.math.*;
import java.util.*;

public interface PaymentGatewayPort {

    PaymentGatewayModel generateUrlPayment(PaymentType paymentType, String currency, BigDecimal amount,
                                           Integer quantity, String productName, String referenceCode,
                                           String transactionId);

    void createPayment(String paymentType, String name, Map<String, String> credentials, String transactionId);

    List<PaymentMethodsDto> paymentMethods(String transactionId);

    void deletePayment(Long paymentTypeId, String transactionId);

    PaymentStatus paymentStatus(PaymentTransaction paymentTransaction, PaymentType paymentType,
                                String transactionId);

    PaymentStatus findByStatus(PaymentType paymentType, String referenceCode, String id, String transactionId);
}
