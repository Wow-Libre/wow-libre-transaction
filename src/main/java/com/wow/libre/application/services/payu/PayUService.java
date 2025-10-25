package com.wow.libre.application.services.payu;

import com.wow.libre.domain.dto.payu.*;
import com.wow.libre.domain.port.in.payu.*;
import com.wow.libre.infrastructure.client.*;
import org.springframework.stereotype.*;

@Service
public class PayUService implements PayuPort {
    private final PayUClient payUClient;

    public PayUService(PayUClient payUClient) {
        this.payUClient = payUClient;
    }

    @Override
    public PayUOrderDetailResponse getOrderDetailByReference(String host, String referenceCode, String apiLogin,
                                                             String apiKey) {
        return payUClient.getOrderDetailByReference(host, referenceCode, apiLogin, apiKey);
    }
}
