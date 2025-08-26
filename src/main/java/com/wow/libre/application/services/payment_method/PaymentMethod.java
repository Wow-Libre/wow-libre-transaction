package com.wow.libre.application.services.payment_method;

import com.wow.libre.domain.model.*;
import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public abstract class PaymentMethod {
    public abstract PaymentGatewayModel payment(Long idMethodGateway, String currency, Double amount,
                                                Integer quantity, String productName,
                                                String referenceCode, String transactionId);

    public abstract void vinculate(PaymentGatewaysEntity paymentMethod,
                                   Map<String, String> credentials, String transactionId);

    public abstract void delete(PaymentGatewaysEntity paymentMethod, String transactionId);
}
