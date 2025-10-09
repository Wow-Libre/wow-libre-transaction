package com.wow.libre.application.services.payment_method;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.infrastructure.entities.*;

import java.math.*;
import java.util.*;

public abstract class PaymentMethod {
    public abstract PaymentGatewayModel payment(Long idMethodGateway, String currency, BigDecimal amount,
                                                Integer quantity, String productName,
                                                String referenceCode, String transactionId);

    public abstract void vinculate(PaymentGatewaysEntity paymentMethod,
                                   Map<String, String> credentials, String transactionId);

    public abstract void delete(PaymentGatewaysEntity paymentMethod, String transactionId);

    public abstract boolean validateCredentials(PaymentGatewaysEntity paymentGateway,
                                                PaymentTransaction paymentTransaction, String transactionId);

    public abstract PaymentStatus paymentStatus(PaymentTransaction paymentTransaction, String transactionId);
}
