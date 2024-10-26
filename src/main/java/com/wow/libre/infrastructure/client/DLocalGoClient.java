package com.wow.libre.infrastructure.client;

import com.wow.libre.domain.constant.Constants;
import com.wow.libre.domain.dto.client.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.infrastructure.conf.*;
import org.slf4j.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

import java.util.*;

@Component
public class DLocalGoClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DLocalGoClient.class);

    private final RestTemplate restTemplate;
    private final Configurations configurations;


    public DLocalGoClient(RestTemplate restTemplate, Configurations configurations) {
        this.restTemplate = restTemplate;
        this.configurations = configurations;
    }

    public CreatePaymentResponse createPayment(CreatePaymentRequest request, String transactionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.HEADER_TRANSACTION_ID, transactionId);
        headers.set("authorization", String.format("Bearer %s:%s", configurations.getApiKeyDLocalGoHost(),
                configurations.getApiSecretDLocalGoHost()));

        HttpEntity<CreatePaymentRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<CreatePaymentResponse> response = restTemplate.exchange(String.format("%s/v1/payments",
                            configurations.getDLocalGoHost()), HttpMethod.POST, entity,
                    new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[DLocalGoClient] [createPayment] Client/Server Error: {}. The request failed with a " +
                            "client or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[DLocalGoClient] [createPayment] Unexpected Error: {}. An unexpected error occurred " +
                            "during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);

    }


}
