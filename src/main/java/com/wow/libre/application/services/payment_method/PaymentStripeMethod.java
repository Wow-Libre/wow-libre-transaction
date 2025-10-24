package com.wow.libre.application.services.payment_method;

import com.stripe.*;
import com.stripe.exception.*;
import com.stripe.model.checkout.*;
import com.stripe.net.*;
import com.stripe.param.checkout.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.out.stripe_credentials.*;
import com.wow.libre.infrastructure.entities.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

@Component
public class PaymentStripeMethod extends PaymentMethod {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentStripeMethod.class);

    private final ObtainStripeCredentials obtainStripeCredentials;
    private final SaveStripeCredentials saveStripeCredentials;

    public PaymentStripeMethod(ObtainStripeCredentials obtainStripeCredentials,
                               SaveStripeCredentials saveStripeCredentials) {
        this.obtainStripeCredentials = obtainStripeCredentials;
        this.saveStripeCredentials = saveStripeCredentials;
    }

    @Override
    public PaymentGatewayModel payment(Long idMethodGateway, String currency, BigDecimal amount, Integer quantity,
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
                    .setClientReferenceId(referenceCode)
                    .setPaymentIntentData(
                            SessionCreateParams.PaymentIntentData.builder()
                                    .putMetadata("referenceCode", referenceCode) // 👈 Aquí va tu ID interno
                                    .build()
                    ).addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(quantity.longValue())
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(currency)
                                                    .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(productName)
                                                                    .build()).build())
                                    .build()
                    )
                    .build();
            Session session = Session.create(params);
            return PaymentGatewayModel.builder()
                    .id(session.getId())
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
        stripeCredentialsEntity.setWebhookUrl(credentials.get("webhookSecret"));
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

    @Override
    public boolean validateCredentials(PaymentGatewaysEntity paymentGateway,
                                       PaymentTransaction paymentTransaction,
                                       String transactionId) {

        StripeCredentialsEntity stripeCred = obtainStripeCredentials
                .findByPayUCredentials(paymentGateway.getId(), transactionId)
                .orElseThrow(() -> new InternalException("Stripe Credentials Not Found", transactionId));

        final String payload = paymentTransaction.getStripePayment().getPayloadStripe();
        final String sigHeader = paymentTransaction.getSign();
        final String secret = stripeCred.getWebHookSecret();
        try {
            Webhook.constructEvent(payload, sigHeader, secret);
            return true;
        } catch (SignatureVerificationException e) {
            LOGGER.error("❌ Firma inválida del webhook: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public PaymentStatus paymentStatus(PaymentTransaction paymentTransaction, String transactionId) {

        String status = paymentTransaction.getStripePayment().getStatus();
        Boolean paid = paymentTransaction.getStripePayment().getPaid();
        Boolean captured = paymentTransaction.getStripePayment().getCaptured();

        boolean pagoExitoso = "succeeded".equalsIgnoreCase(status)
                && Boolean.TRUE.equals(paid)
                && Boolean.TRUE.equals(captured);

        if (!pagoExitoso) {
            LOGGER.warn("❌ Pago no exitoso. Status: {}, Paid: {}, Captured: {}", status, paid, captured);
            return PaymentStatus.REJECTED;
        }

        return PaymentStatus.APPROVED;
    }


}
