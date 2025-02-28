package com.wow.libre.application.services.payment;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.payment.*;
import com.wow.libre.domain.port.in.subscription.*;
import com.wow.libre.domain.port.in.transaction.*;
import com.wow.libre.infrastructure.conf.*;
import com.wow.libre.infrastructure.entities.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.nio.charset.*;
import java.security.*;
import java.util.*;

@Service
public class PaymentService implements PaymentPort {
    public static final String SUCCESS_REDIRECT_URL = "https://www.wowlibre.com/profile/purchases";
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
    private final TransactionPort transactionPort;
    private final Configurations configurations;
    private final SubscriptionPort subscriptionPort;

    public PaymentService(TransactionPort transactionPort, Configurations configurations,
                          SubscriptionPort subscriptionPort) {
        this.transactionPort = transactionPort;
        this.configurations = configurations;
        this.subscriptionPort = subscriptionPort;
    }

    @Override
    public CreatePaymentRedirectDto createPayment(Long userId, CreatePaymentDto createPaymentDto,
                                                  String transactionId) {

        return null;
    }

    @Override
    public void processPayment(PaymentTransaction paymentTransaction, String transactionId) {

        Optional<TransactionEntity> transaction =
                transactionPort.findByReferenceNumber(paymentTransaction.getReferenceSale(), transactionId);

        if (transaction.isEmpty()) {
            throw new InternalException("", transactionId);
        }

        PaymentStatus paymentStatus = PaymentStatus.getType(paymentTransaction.getResponseMessagePol());
        TransactionEntity transactionUpdate = transaction.get();

        switch (paymentStatus) {
            case APPROVED:
                transactionUpdate.setStatus(TransactionStatus.PAID.getType());
                transactionUpdate.setSend(true);
                transactionUpdate.setPaymentMethod(paymentTransaction.getPaymentMethodName());
                transactionPort.save(transactionUpdate, transactionId);
                subscriptionPort.createSubscription(transactionUpdate.getUserId(), transactionId);
                break;
            case REJECTED:
                transactionUpdate.setStatus(TransactionStatus.REJECTED.getType());
                transactionUpdate.setSend(true);
                transactionUpdate.setPaymentMethod(paymentTransaction.getPaymentMethodName());
                transactionPort.save(transactionUpdate, transactionId);
                break;
            default:
                transactionUpdate.setStatus(TransactionStatus.REJECTED.getType());
                transactionUpdate.setSend(true);
                transactionUpdate.setPaymentMethod(paymentTransaction.getPaymentMethodName());
                transactionPort.save(transactionUpdate, transactionId);
                break;
        }
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
        final String merchantId = configurations.getPayUMerchantId();
        final String accountId = configurations.getPayUAccountId();
        final String referenceCode = paymentApplicableModel.orderId;
        final String taxValue = paymentApplicableModel.tax;
        final String amount = String.valueOf(paymentApplicableModel.amount.intValue());
        final String currency = paymentApplicableModel.currency;
        final String returnTax = paymentApplicableModel.returnTax;

        final String concatenatedString =
                apiKey + "~" + merchantId + "~" + referenceCode + "~" + amount + "~" + currency;
        final String signature = generateHash(concatenatedString);

        return new CreatePaymentRedirectDto(configurations.getPayHost(), configurations.getPayUConfirmUrl(),
                SUCCESS_REDIRECT_URL, email, signature, currency, returnTax, taxValue, amount,
                referenceCode, paymentApplicableModel.description, accountId, merchantId,
                configurations.getPayUIsTest());
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
