package com.wow.libre.application.services.payment_method;

import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.port.out.payu_credentials.*;
import com.wow.libre.domain.port.out.stripe_credentials.*;

public class PaymentMethodFactory {

    public static PaymentMethod paymentMethod(PaymentType paymentType,
                                              ObtainPayuCredentials payuCredentials,
                                              ObtainStripeCredentials stripeCredentials,
                                              SavePayUCredentials savePayUCredentials,
                                              SaveStripeCredentials saveStripeCredentials,
                                              String transactionId) {
        return switch (paymentType) {
            case PAYU -> new PaymentPayUMethod(payuCredentials, savePayUCredentials);
            case STRIPE -> new PaymentStripeMethod(stripeCredentials, saveStripeCredentials);
            default -> throw new InternalException("Invalid Payment Method", transactionId);
        };
    }
}
