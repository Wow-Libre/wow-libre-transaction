package com.wow.libre.application.services.payment;

import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.dto.client.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.payment.*;
import com.wow.libre.domain.port.in.transaction.*;
import com.wow.libre.infrastructure.client.*;
import org.springframework.stereotype.*;

@Service
public class PaymentService implements PaymentPort {
    private final TransactionPort transactionPort;
    private final DLocalGoClient client;

    public PaymentService(TransactionPort transactionPort, DLocalGoClient client) {
        this.transactionPort = transactionPort;
        this.client = client;
    }

    @Override
    public CreatePaymentRedirectDto createPayment(Long userId, CreatePaymentDto createPaymentDto,
                                                  String transactionId) {


        PaymentApplicableModel paymentApplicableModel = transactionPort.isRealPaymentApplicable(
                TransactionModel.builder()
                        .referenceNumber(createPaymentDto.getReferenceNumber())
                        .accountId(createPaymentDto.getAccountId())
                        .serverId(createPaymentDto.getServerId())
                        .isSubscription(createPaymentDto.getIsSubscription())
                        .userId(userId).build(), transactionId);

        if (paymentApplicableModel.isPayment()) {
            CreatePaymentRequest request = CreatePaymentRequest.
                    builder().amount(paymentApplicableModel.amount())
                    .currency(paymentApplicableModel.currency())
                    .description("Donation Server")
                    .orderId(paymentApplicableModel.orderId())
                    .notificationUrl("http://localhost:8090/")
                    .successUrl("http://localhost:3000/store/000001")
                    .backUrl("http://localhost:3000/store")
                    .build();

            CreatePaymentResponse createPaymentResponse = client.createPayment(request, transactionId);

            transactionPort.assignmentPaymentId(paymentApplicableModel.orderId(), createPaymentResponse.getId(),
                    transactionId);

            return new CreatePaymentRedirectDto(createPaymentResponse.getRedirectUrl(),
                    createPaymentResponse.getSuccessUrl(), createPaymentResponse.getBackUrl());
        }

        return new CreatePaymentRedirectDto("", "", "");
    }

    @Override
    public void processPayment(String orderId, String transactionId) {

    }
}
