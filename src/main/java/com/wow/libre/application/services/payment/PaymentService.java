package com.wow.libre.application.services.payment;

import com.wow.libre.application.services.transaction.*;
import com.wow.libre.domain.*;
import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.dto.client.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.payment.*;
import com.wow.libre.domain.port.in.transaction.*;
import com.wow.libre.infrastructure.client.*;
import com.wow.libre.infrastructure.entities.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

@Service
public class PaymentService implements PaymentPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

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
                        .isSubscription(createPaymentDto.getIsSubscription())
                        .userId(userId).build(), transactionId);

        if (paymentApplicableModel.isPayment()) {
            CreatePaymentRequest request = CreatePaymentRequest.
                    builder().amount(paymentApplicableModel.amount())
                    .currency(paymentApplicableModel.currency())
                    .description(paymentApplicableModel.description())
                    .orderId(paymentApplicableModel.orderId())
                    .notificationUrl("https://883e-181-51-34-114.ngrok-free.app/api/payment/notification")
                    .successUrl("http://localhost:3000/profile/purchases")
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
    public void processPayment(String paymentId, String transactionId) {
        PaymentDetailResponse response = client.paymentDetail(paymentId, transactionId);

        if (response == null) {
            LOGGER.error("[PaymentService] [processPayment] Error Order Not Found PaymentId {}", paymentId);
            throw new InternalException("There is no transaction created", transactionId);
        }

        TransactionEntity transaction = transactionPort.transaction(response.getOrderId(), transactionId);
        transaction.setStatus(response.getStatus());
        transactionPort.save(transaction, transactionId);
    }
}
