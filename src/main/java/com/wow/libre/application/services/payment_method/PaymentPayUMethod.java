package com.wow.libre.application.services.payment_method;

import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.out.payu_credentials.*;
import com.wow.libre.infrastructure.entities.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.nio.charset.*;
import java.security.*;
import java.util.*;

@Component
public class PaymentPayUMethod extends PaymentMethod {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentPayUMethod.class);

    private final ObtainPayuCredentials payuCredentials;
    private final SavePayUCredentials savePayUCredentials;


    public PaymentPayUMethod(ObtainPayuCredentials payuCredentials, SavePayUCredentials savePayUCredentials) {
        this.payuCredentials = payuCredentials;
        this.savePayUCredentials = savePayUCredentials;
    }

    @Override
    public PaymentGatewayModel payment(Long idMethodGateway, String currency, Double amount,
                                       Integer quantity, String productName,
                                       String referenceCode, String transactionId) {

        Optional<PayuCredentialsEntity> payUCredential = payuCredentials.findByPayUCredentials(idMethodGateway,
                transactionId);

        if (payUCredential.isEmpty()) {
            LOGGER.error("PayUGateway Credentials Not Found");
            throw new InternalException("PayUGateway Credentials Invalid", transactionId);
        }

        PayuCredentialsEntity payUCredentials = payUCredential.get();
        final String apiKey = payUCredentials.getApiKey();
        final String merchantId = payUCredentials.getMerchantId();
        final String accountId = payUCredentials.getAccountId();

        final String concatenatedString =
                apiKey + "~" + merchantId + "~" + referenceCode + "~" + amount + "~" + currency;
        final String signature = generateHash(concatenatedString);

        return PaymentGatewayModel.builder()
                .redirect(payUCredentials.getHost())
                .successUrl(payUCredentials.getSuccessUrl())
                .cancelUrl(payUCredentials.getCancelUrl())
                .webhookUrl(payUCredentials.getWebhookUrl())
                .signature(signature)
                .payu(PaymentGatewayModel.PayU.builder()
                        .accountId(accountId).merchantId(merchantId)
                        .build())
                .build();
    }

    @Override
    public void vinculate(PaymentGatewaysEntity paymentMethod,
                          Map<String, String> credentials, String transactionId) {

        PayuCredentialsEntity payuCredentialsEntity = new PayuCredentialsEntity();
        payuCredentialsEntity.setAccountId(credentials.get("accountId"));
        payuCredentialsEntity.setHost(credentials.get("host"));
        payuCredentialsEntity.setCancelUrl(credentials.get("cancelUrl"));
        payuCredentialsEntity.setGateway(paymentMethod);
        payuCredentialsEntity.setSuccessUrl(credentials.get("successUrl"));
        payuCredentialsEntity.setApiKey(credentials.get("apiKey"));
        payuCredentialsEntity.setApiLogin(credentials.get("apiLogin"));
        payuCredentialsEntity.setKeyPublic(credentials.get("keyPublic"));
        payuCredentialsEntity.setMerchantId(credentials.get("merchantId"));
        payuCredentialsEntity.setWebhookUrl(credentials.get("webhookUrl"));
        savePayUCredentials.save(payuCredentialsEntity, transactionId);
    }

    @Override
    public void delete(PaymentGatewaysEntity paymentMethod, String transactionId) {
        Optional<PayuCredentialsEntity> payuCredentialsEntity =
                payuCredentials.findByPayUCredentials(paymentMethod.getId(), transactionId);

        if (payuCredentialsEntity.isEmpty()) {
            LOGGER.error("PayU Credentials Not Found {} TransactionId {}", paymentMethod.getId(), transactionId);
            throw new InternalException("PayU Credentials Not Found", transactionId);
        }

        savePayUCredentials.delete(payuCredentialsEntity.get(), transactionId);
    }

    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error generating hash: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar hash: " + e.getMessage());
        }
    }
}
