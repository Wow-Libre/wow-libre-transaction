package com.wow.libre.application.services.payment;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.payment.*;
import com.wow.libre.domain.port.in.transaction.*;
import com.wow.libre.infrastructure.conf.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.nio.charset.*;
import java.security.*;

@Service
public class PaymentService implements PaymentPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    private final TransactionPort transactionPort;
    private final Configurations configurations;

    public PaymentService(TransactionPort transactionPort, Configurations configurations) {
        this.transactionPort = transactionPort;
        this.configurations = configurations;
    }

    @Override
    public CreatePaymentRedirectDto createPayment(Long userId, CreatePaymentDto createPaymentDto,
                                                  String transactionId) {


        return null;
    }

    @Override
    public void processPayment(String paymentId, String transactionId) {


    }

    @Override
    public CreatePaymentRedirectDto createSubscription(Long userId, String email, CreatePaymentDto createPaymentDto,
                                                       String transactionId) {

        PaymentApplicableModel paymentApplicableModel =
                transactionPort.isRealPaymentApplicable(TransactionModel.builder()
                        .isSubscription(createPaymentDto.getIsSubscription())
                        .accountId(createPaymentDto.getAccountId())
                        .serverId(createPaymentDto.getServerId())
                        .userId(userId).build(), transactionId);

        final String apiKey = configurations.getPayUApiKey();
        final String merchantId = "508029";
        final String accountId = "512321";
        final String referenceCode = paymentApplicableModel.orderId;
        final String taxValue = "0";
        final String amount = String.valueOf(paymentApplicableModel.amount.intValue());
        final String currency = paymentApplicableModel.currency;

        final String concatenatedString =
                apiKey + "~" + merchantId + "~" + referenceCode + "~" + amount + "~" + currency;

        final String signature = generateHash(concatenatedString);

        return new CreatePaymentRedirectDto(configurations.getPayHost(), configurations.getPayUConfirmUrl(), "www" +
                ".wowlibre.com", email, signature, currency, "0", taxValue, amount, referenceCode,
                paymentApplicableModel.description, accountId, merchantId, "1");
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
            throw new RuntimeException("Error al generar hash: " + e.getMessage());
        }
    }
}
