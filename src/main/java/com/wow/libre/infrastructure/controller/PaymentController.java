package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.port.in.payment.*;
import com.wow.libre.domain.shared.*;
import jakarta.validation.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.*;
import java.security.*;
import java.util.*;

import static com.wow.libre.domain.constant.Constants.*;
import static java.lang.Double.*;
import static java.lang.Integer.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PaymentController.class);
    private final PaymentPort paymentPort;

    public PaymentController(PaymentPort paymentPort) {
        this.paymentPort = paymentPort;
    }


    @PostMapping(value = "/notification", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<GenericResponse<Void>> notification(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam Map<String, String> params) {

        final String referenceSale = params.get("reference_sale");
        final String valueRaw = params.get("value");
        final String statePol = params.get("state_pol");

        // === Firma OK: procesar pago ===
        paymentPort.processPayment(PaymentTransaction.builder()
                .date(params.get("date"))
                .sign(params.get("sign"))
                .statePol(statePol)
                .paymentMethodType(parseInt(params.get("payment_method_type")))
                .franchise(params.get("franchise"))
                .operationDate(params.get("operation_date"))
                .paymentRequestState(params.get("payment_request_state"))
                .bankId(params.get("bank_id"))
                .paymentMethod(params.get("payment_method"))
                .transactionId(params.get("transaction_id"))
                .transactionDate(params.get("transaction_date"))
                .exchangeRate(parseDouble(params.get("exchange_rate")))
                .ip(params.get("ip"))
                .referencePol(params.get("reference_pol"))
                .ccHolder(params.get("cc_holder"))
                .tax(parseDouble(params.get("tax")))
                .transactionType(params.get("transaction_type"))
                .shippingCountry(params.get("shipping_country"))
                .billingCountry(params.get("billing_country"))
                .authorizationCode(params.get("authorization_code"))
                .currency(params.get("currency"))
                .merchantId(params.get("merchant_id"))
                .ccNumber(params.get("cc_number"))
                .installmentsNumber(parseInt(params.get("installments_number")))
                .value(valueRaw)
                .paymentMethodName(params.get("payment_method_name"))
                .emailBuyer(params.get("email_buyer"))
                .responseMessagePol(params.get("response_message_pol"))
                .accountId(params.get("account_id"))
                .referenceSale(referenceSale)
                .sign(params.get("sign"))
                .build(), PaymentType.PAYU, transactionId);

        LOGGER.info("✅ Notificación procesada correctamente. referenceSale=[{}], value=[{}], statePol=[{}]",
                referenceSale, valueRaw, statePol);

        return ResponseEntity.ok(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


    // === FUNCIÓN AUXILIAR PARA GENERAR MD5 ===
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


    @PostMapping
    public ResponseEntity<GenericResponse<CreatePaymentRedirectDto>> createPayment(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestHeader(name = HEADER_EMAIL) final String email,
            @RequestBody @Valid CreatePaymentDto createPaymentDto) {

        CreatePaymentRedirectDto payment = paymentPort.createPayment(userId, email, createPaymentDto,
                transactionId);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponseBuilder<>(payment, transactionId).created().build());
    }
}
