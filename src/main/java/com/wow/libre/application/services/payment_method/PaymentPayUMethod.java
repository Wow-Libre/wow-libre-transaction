package com.wow.libre.application.services.payment_method;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.out.payu_credentials.*;
import com.wow.libre.infrastructure.entities.*;
import com.wow.libre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.math.*;
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
    public PaymentGatewayModel payment(Long idMethodGateway, String currency, BigDecimal amount,
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

        String formattedAmount = amount.toPlainString();

// === Construcción de cadena para checkout ===
        final String concatenatedString =
                apiKey + "~" + merchantId + "~" + referenceCode + "~" + formattedAmount + "~" + currency;
        final String signature = PayUSignatureUtil.md5(concatenatedString);
        // === LOGS DETALLADOS ===
        LOGGER.info("=== 🛒 FIRMA GENERADA EN BACKEND (CHECKOUT) ===");
        LOGGER.info("apiKey usado: [{}]", apiKey);
        LOGGER.info("merchantId usado: [{}]", merchantId);
        LOGGER.info("accountId usado: [{}]", accountId);
        LOGGER.info("referenceCode usado: [{}]", referenceCode);

        LOGGER.info("amount recibido (BigDecimal): [{}]", amount);
        LOGGER.info("formattedAmount usado en firma: [{}]", formattedAmount);
        LOGGER.info("currency usado: [{}]", currency);

        LOGGER.info("Cadena exacta para firma (checkout): [{}]", concatenatedString);
        LOGGER.info("Firma MD5 generada: [{}]", signature);

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

    @Override
    public boolean validateCredentials(PaymentGatewaysEntity paymentGateway,
                                       PaymentTransaction paymentTransaction, String transactionId) {
        Optional<PayuCredentialsEntity> payuCredentialsEntity =
                payuCredentials.findByPayUCredentials(paymentGateway.getId(), transactionId);

        if (payuCredentialsEntity.isEmpty()) {
            LOGGER.error("[validateCredentials] PayU Credentials Not Found {} TransactionId {}",
                    paymentGateway.getId(), transactionId);
            throw new InternalException("PayU Credentials Not Found", transactionId);
        }

        PayuCredentialsEntity payUCredentials = payuCredentialsEntity.get();
        final String apiKey = payUCredentials.getApiKey();

        final String merchantId = paymentTransaction.getMerchantId();
        final String referenceSale = paymentTransaction.getReferenceSale();
        final String valueRaw = paymentTransaction.getValue();
        final String currency = paymentTransaction.getCurrency();
        final String statePol = paymentTransaction.getStatePol();
        final String signReceived = paymentTransaction.getSign();

        final String normalizedValue = PayUSignatureUtil.normalizeValue(valueRaw);

        final String signatureString = PayUSignatureUtil.buildSignatureString(
                apiKey, merchantId, referenceSale, normalizedValue, currency, statePol);

        final String expectedSign = PayUSignatureUtil.md5(signatureString);

        if (signReceived == null || !signReceived.equalsIgnoreCase(expectedSign)) {
            LOGGER.error("❌ Firma inválida. Notificación rechazada. " +
                            "expected=[{}], received=[{}], cadena=[{}]",
                    expectedSign, signReceived, signatureString);
            return false;
        }

        return true;
    }


}
