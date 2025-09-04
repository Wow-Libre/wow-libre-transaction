package com.wow.libre.application.services.payment_method;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.enums.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.payment_gateway.*;
import com.wow.libre.domain.port.out.payment_gateway.*;
import com.wow.libre.domain.port.out.payu_credentials.*;
import com.wow.libre.domain.port.out.stripe_credentials.*;
import com.wow.libre.infrastructure.entities.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.math.*;
import java.time.*;
import java.util.*;

import static com.wow.libre.domain.enums.PaymentType.*;

@Service
public class PaymentGatewayService implements PaymentGatewayPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentGatewayService.class);

    private final ObtainPaymentGateway obtainPaymentGateway;
    private final ObtainStripeCredentials obtainStripeCredentials;
    private final ObtainPayuCredentials obtainPayuCredentials;
    private final SavePaymentGateway savePaymentGateway;
    private final SavePayUCredentials savePayUCredentials;
    private final SaveStripeCredentials saveStripeCredentials;

    public PaymentGatewayService(ObtainPaymentGateway obtainPaymentGateway,
                                 ObtainStripeCredentials obtainStripeCredentials,
                                 ObtainPayuCredentials obtainPayuCredentials,
                                 SavePaymentGateway savePaymentGateway, SavePayUCredentials savePayUCredentials,
                                 SaveStripeCredentials saveStripeCredentials) {
        this.obtainPaymentGateway = obtainPaymentGateway;
        this.obtainStripeCredentials = obtainStripeCredentials;
        this.obtainPayuCredentials = obtainPayuCredentials;
        this.savePaymentGateway = savePaymentGateway;
        this.savePayUCredentials = savePayUCredentials;
        this.saveStripeCredentials = saveStripeCredentials;
    }

    @Override
    public PaymentGatewayModel generateUrlPayment(PaymentType paymentType, String currency, BigDecimal amount,
                                                  Integer quantity, String productName, String referenceCode,
                                                  String transactionId) {

        Optional<PaymentGatewaysEntity> paymentAvailable = obtainPaymentGateway.findByPaymentType(paymentType,
                transactionId);

        if (paymentAvailable.isEmpty()) {
            LOGGER.error("Payment method {} not available", paymentType);
            throw new InternalException("Method payment is disable", transactionId);
        }

        PaymentGatewaysEntity paymentGateway = paymentAvailable.get();

        PaymentMethod paymentMethod = PaymentMethodFactory.paymentMethod(paymentType, obtainPayuCredentials,
                obtainStripeCredentials, savePayUCredentials, saveStripeCredentials, transactionId);

        return paymentMethod.payment(paymentGateway.getId(), currency, amount, quantity, productName, referenceCode,
                transactionId);
    }

    @Override
    @Transactional
    public void createPayment(String paymentType, String name, Map<String, String> credentials, String transactionId) {

        PaymentType paymentMethodType = PaymentType.getType(paymentType);

        if (paymentMethodType == null || paymentMethodType.equals(NOT_MAPPED)) {
            throw new InternalException("Payment Method Type Invalid", transactionId);
        }

        Optional<PaymentGatewaysEntity> paymentAvailable = obtainPaymentGateway.findByPaymentType(paymentMethodType,
                transactionId);

        if (paymentAvailable.isPresent()) {
            LOGGER.error("Payment method {} already exist", paymentType);
            throw new InternalException("Payment Method Exist", transactionId);
        }


        PaymentGatewaysEntity paymentMethod = new PaymentGatewaysEntity();
        paymentMethod.setName(name);
        paymentMethod.setActive(true);
        paymentMethod.setCreatedAt(LocalDateTime.now());
        paymentMethod.setType(paymentMethodType);
        savePaymentGateway.save(paymentMethod, transactionId);
        PaymentMethod paymentMethodFactory = PaymentMethodFactory.paymentMethod(paymentMethodType,
                obtainPayuCredentials, obtainStripeCredentials, savePayUCredentials, saveStripeCredentials,
                transactionId);
        paymentMethodFactory.vinculate(paymentMethod, credentials, transactionId);
    }

    @Override
    public List<PaymentMethodsDto> paymentMethods(String transactionId) {

        List<PaymentGatewaysEntity> paymentMethodGateways = obtainPaymentGateway.findByIsActiveIsTrue(transactionId);

        return paymentMethodGateways.stream()
                .map(payment -> new PaymentMethodsDto(payment.getId(), payment.getType(),
                        payment.getName(), payment.getCreatedAt())).toList();
    }

    @Override
    public void deletePayment(Long paymentTypeId, String transactionId) {

        Optional<PaymentGatewaysEntity> paymentGatewayDelete = obtainPaymentGateway.findByPaymentTypeId(paymentTypeId,
                transactionId);

        if (paymentGatewayDelete.isEmpty()) {
            LOGGER.error("Payment method id {} not exist", paymentTypeId);
            throw new InternalException("Payment Method Not Exist", transactionId);
        }

        PaymentMethod paymentMethodFactory = PaymentMethodFactory.paymentMethod(paymentGatewayDelete.get().getType(),
                obtainPayuCredentials, obtainStripeCredentials, savePayUCredentials, saveStripeCredentials,
                transactionId);
        paymentMethodFactory.delete(paymentGatewayDelete.get(), transactionId);
        savePaymentGateway.delete(paymentGatewayDelete.get(), transactionId);
    }

    @Override
    public boolean isValidPaymentSignature(PaymentTransaction paymentTransaction, PaymentType paymentType,
                                           String transactionId) {
        Optional<PaymentGatewaysEntity> paymentAvailable = obtainPaymentGateway.findByPaymentType(paymentType,
                transactionId);

        if (paymentAvailable.isEmpty()) {
            LOGGER.error("[isValidPaymentSignature] Payment method {} not available", paymentType);
            throw new InternalException("Method payment is disable", transactionId);
        }

        PaymentMethod paymentMethodFactory = PaymentMethodFactory.paymentMethod(paymentType,
                obtainPayuCredentials, obtainStripeCredentials, savePayUCredentials, saveStripeCredentials,
                transactionId);

        return paymentMethodFactory.validateCredentials(paymentAvailable.get(), paymentTransaction, transactionId);
    }
}
