package com.wow.libre.application.services.payment_method;

import com.wow.libre.domain.port.in.payu.PayuPort;
import com.wow.libre.domain.port.out.payu_credentials.ObtainPayuCredentials;
import com.wow.libre.domain.port.out.payu_credentials.SavePayUCredentials;
import com.wow.libre.domain.port.out.stripe_credentials.ObtainStripeCredentials;
import com.wow.libre.domain.port.out.stripe_credentials.SaveStripeCredentials;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceLocator {

    private final ObtainPayuCredentials obtainPayuCredentials;
    private final ObtainStripeCredentials obtainStripeCredentials;
    private final SavePayUCredentials savePayUCredentials;
    private final SaveStripeCredentials saveStripeCredentials;
    private final PayuPort payuPort;

    public PaymentServiceLocator(ObtainPayuCredentials obtainPayuCredentials,
            ObtainStripeCredentials obtainStripeCredentials,
            SavePayUCredentials savePayUCredentials,
            SaveStripeCredentials saveStripeCredentials,
            PayuPort payuPort) {
        this.obtainPayuCredentials = obtainPayuCredentials;
        this.obtainStripeCredentials = obtainStripeCredentials;
        this.savePayUCredentials = savePayUCredentials;
        this.saveStripeCredentials = saveStripeCredentials;
        this.payuPort = payuPort;
    }

    public ObtainPayuCredentials getObtainPayuCredentials() {
        return obtainPayuCredentials;
    }

    public ObtainStripeCredentials getObtainStripeCredentials() {
        return obtainStripeCredentials;
    }

    public SavePayUCredentials getSavePayUCredentials() {
        return savePayUCredentials;
    }

    public SaveStripeCredentials getSaveStripeCredentials() {
        return saveStripeCredentials;
    }

    public PayuPort getPayuPort() {
        return payuPort;
    }
}
