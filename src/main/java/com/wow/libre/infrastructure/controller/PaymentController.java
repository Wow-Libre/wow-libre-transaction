package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.payment.*;
import com.wow.libre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.wow.libre.domain.constant.Constants.*;
import static java.lang.Double.*;
import static java.lang.Integer.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentPort paymentPort;

    public PaymentController(PaymentPort paymentPort) {
        this.paymentPort = paymentPort;
    }

    @PostMapping(value = "/notification", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<GenericResponse<Void>> notificationPayment2(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam Map<String, String> params) {

        PaymentTransaction request = new PaymentTransaction();
        request.setDate(params.get("date"));
        request.setPaymentMethodType(parseInt(params.get("payment_method_type")));
        request.setFranchise(params.get("franchise"));
        request.setOperationDate(params.get("operation_date"));
        request.setPaymentRequestState(params.get("payment_request_state"));
        request.setBankId(params.get("bank_id"));
        request.setPaymentMethod(params.get("payment_method"));
        request.setTransactionId(params.get("transaction_id"));
        request.setTransactionDate(params.get("transaction_date"));
        request.setExchangeRate(parseDouble(params.get("exchange_rate")));
        request.setIp(params.get("ip"));
        request.setReferencePol(params.get("reference_pol"));
        request.setCcHolder(params.get("cc_holder"));
        request.setTax(parseDouble(params.get("tax")));
        request.setTransactionType(params.get("transaction_type"));
        request.setStatePol(parseInt(params.get("state_pol")));
        request.setShippingCountry(params.get("shipping_country"));
        request.setBillingCountry(params.get("billing_country"));
        request.setAuthorizationCode(params.get("authorization_code"));
        request.setCurrency(params.get("currency"));
        request.setCcNumber(params.get("cc_number"));
        request.setInstallmentsNumber(parseInt(params.get("installments_number")));
        request.setValue(parseDouble(params.get("value")));
        request.setPaymentMethodName(params.get("payment_method_name"));
        request.setEmailBuyer(params.get("email_buyer"));
        request.setResponseMessagePol(params.get("response_message_pol"));
        request.setAccountId(params.get("account_id"));
        request.setReferenceSale(params.get("reference_sale"));
        request.setSign(params.get("sign"));

        paymentPort.processPayment(request, transactionId);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


    @PostMapping
    public ResponseEntity<GenericResponse<CreatePaymentRedirectDto>> createSubscription(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestHeader(name = HEADER_EMAIL) final String email,
            @RequestBody @Valid CreatePaymentDto createPaymentDto) {

        CreatePaymentRedirectDto payment = paymentPort.createSubscription(userId, email, createPaymentDto,
                transactionId);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponseBuilder<>(payment, transactionId).created().build());
    }
}
