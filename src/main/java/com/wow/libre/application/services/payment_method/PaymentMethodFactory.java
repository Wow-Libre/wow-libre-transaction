package com.wow.libre.application.services.payment_method;

import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import org.springframework.stereotype.*;

@Component
public class PaymentMethodFactory {

    private final PaymentServiceLocator serviceLocator;

    public PaymentMethodFactory(PaymentServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public PaymentMethod createPaymentMethod(PaymentType paymentType, String transactionId) {
        return switch (paymentType) {
            case PAYU -> new PaymentPayUMethod(
                    serviceLocator.getObtainPayuCredentials(),
                    serviceLocator.getSavePayUCredentials(),
                    serviceLocator.getPayuPort());
            case STRIPE -> new PaymentStripeMethod(
                    serviceLocator.getObtainStripeCredentials(),
                    serviceLocator.getSaveStripeCredentials());
            default -> throw new InternalException("Invalid Payment Method", transactionId);
        };
    }
}
