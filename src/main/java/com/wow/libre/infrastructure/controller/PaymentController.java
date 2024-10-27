package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.payment.*;
import com.wow.libre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;



import static com.wow.libre.domain.constant.Constants.HEADER_TRANSACTION_ID;
import static com.wow.libre.domain.constant.Constants.HEADER_USER_ID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentPort paymentPort;

    public PaymentController(PaymentPort paymentPort) {
        this.paymentPort = paymentPort;
    }

    @PostMapping("/notification")
    public ResponseEntity<GenericResponse<Void>> notificationPayment(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid NotificationPaymentDto request) {

        paymentPort.processPayment(request.getPaymentId(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }


    @PostMapping
    public ResponseEntity<GenericResponse<CreatePaymentRedirectDto>> createPayment(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid CreatePaymentDto createPaymentDto) {

        CreatePaymentRedirectDto payment = paymentPort.createPayment(userId, createPaymentDto, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(payment, transactionId).created().build());
    }
}
