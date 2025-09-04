package com.wow.libre.application.services.payment_method;

import com.stripe.*;
import com.stripe.exception.*;
import com.stripe.model.checkout.*;
import com.stripe.param.checkout.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.out.stripe_credentials.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class PaymentStripeMethod extends PaymentMethod {
    private final ObtainStripeCredentials obtainStripeCredentials;
    private final SaveStripeCredentials saveStripeCredentials;

    public PaymentStripeMethod(ObtainStripeCredentials obtainStripeCredentials,
                               SaveStripeCredentials saveStripeCredentials) {
        this.obtainStripeCredentials = obtainStripeCredentials;
        this.saveStripeCredentials = saveStripeCredentials;
    }

    @Override
    public PaymentGatewayModel payment(Long idMethodGateway, String currency, Double amount, Integer quantity,
                                       String productName, String referenceCode, String transactionId) {

        Optional<StripeCredentialsEntity> stripeCredential =
                obtainStripeCredentials.findByPayUCredentials(idMethodGateway,
                        transactionId);

        if (stripeCredential.isEmpty()) {
            throw new InternalException("PayUGateway Credentials Invalid", transactionId);
        }

        try {
            StripeCredentialsEntity stripe = stripeCredential.get();
            Stripe.apiKey = stripe.getApiSecret();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(stripe.getSuccessUrl())
                    .setCancelUrl(stripe.getCancelUrl())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(quantity.longValue())
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(currency)
                                                    .setUnitAmount(amount.longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(productName)
                                                                    .build()).build())
                                    .build()
                    )
                    .build();
            Session session = Session.create(params);
            return PaymentGatewayModel.builder()
                    .redirect(session.getUrl())
                    .build();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void vinculate(PaymentGatewaysEntity paymentMethod,
                          Map<String, String> credentials, String transactionId) {
        StripeCredentialsEntity stripeCredentialsEntity = new StripeCredentialsEntity();
        stripeCredentialsEntity.setGateway(paymentMethod);
        stripeCredentialsEntity.setApiSecret(credentials.get("apiSecret"));
        stripeCredentialsEntity.setApiPublic(credentials.get("apiPublic"));
        stripeCredentialsEntity.setSuccessUrl(credentials.get("successUrl"));
        stripeCredentialsEntity.setCancelUrl(credentials.get("cancelUrl"));
        stripeCredentialsEntity.setWebhookUrl(credentials.get("webhookUrl"));
        saveStripeCredentials.save(stripeCredentialsEntity, transactionId);
    }

    @Override
    public void delete(PaymentGatewaysEntity paymentMethod, String transactionId) {

        Optional<StripeCredentialsEntity> stripeCredentials =
                obtainStripeCredentials.findByPayUCredentials(paymentMethod.getId(), transactionId);

        if (stripeCredentials.isEmpty()) {
            throw new InternalException("Stripe Credentials Not Found", transactionId);
        }

        saveStripeCredentials.delete(stripeCredentials.get(), transactionId);
    }

}
