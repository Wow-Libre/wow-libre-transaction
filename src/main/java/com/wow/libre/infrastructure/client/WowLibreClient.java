package com.wow.libre.infrastructure.client;

import com.fasterxml.jackson.databind.*;
import com.wow.libre.domain.dto.client.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.shared.*;
import com.wow.libre.infrastructure.conf.*;
import com.wow.libre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import java.util.*;

import static com.wow.libre.domain.constant.Constants.*;

@Component
public class WowLibreClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WowLibreClient.class);

    private final RestTemplate restTemplate;
    private final Configurations configurations;
    private final SignatureService signatureService;
    private final ObjectMapper objectMapper;

    public WowLibreClient(RestTemplate restTemplate, Configurations configurations,
                          SignatureService signatureService, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.configurations = configurations;
        this.signatureService = signatureService;
        this.objectMapper = objectMapper;
    }

    public void sendPurchases(Long realmId, Long userId, Long accountId, Double gold,
                              List<ItemQuantityModel> items, String reference,
                              String transactionId) {

        HttpHeaders headers = new HttpHeaders();
        if (transactionId != null && !transactionId.isEmpty()) {
            headers.set(HEADER_TRANSACTION_ID, transactionId);
        }

        CreateTransactionItemsDto requestBody = new CreateTransactionItemsDto(realmId,
                userId, accountId, reference, items, gold);

        addSignatureToHeaders(headers, requestBody, transactionId);

        HttpEntity<CreateTransactionItemsDto> entity = new HttpEntity<>(requestBody, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/transaction/purchase",
                        configurations.getPathLoginWowLibre()))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url,
                    HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return;
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[WowLibreClient] [sendPurchases] Client/Server Error: {}. The request failed with a client " +
                            "or server error. HTTP Status: {}, Response Body: {}", e.getMessage(), e.getStatusCode(),
                    e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[WowLibreClient] [sendPurchases] Unexpected Error: {}. An unexpected error occurred during " +
                            "the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }
        throw new InternalException("Unexpected transaction failure", transactionId);

    }

    public void sendBenefitsPremium(SubscriptionBenefitsRequest request, String transactionId) {
        HttpHeaders headers = new HttpHeaders();
        if (transactionId != null && !transactionId.isEmpty()) {
            headers.set(HEADER_TRANSACTION_ID, transactionId);
        }

        addSignatureToHeaders(headers, request, transactionId);

        HttpEntity<SubscriptionBenefitsRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/transaction/subscription-benefits",
                        configurations.getPathLoginWowLibre()))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return;
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [sendBenefitsPremium]  Client/Server Error: {}. Error with server " +
                            "client getting  associated guilds. HTTP Status: {}, Response Body: {}", e.getMessage(),
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [sendBenefitsPremium] Unexpected Error: {}. An unexpected error " +
                    "occurred during the transaction with ID: {}.", e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    private void addSignatureToHeaders(HttpHeaders headers, Object requestBody, String transactionId) {
        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            String signature = signatureService.generateSignatureForJson(jsonBody);
            headers.set(HEADER_SIGNATURE, signature);

            LOGGER.error("[WowLibreClient] Generated signature: {} JsonBody {}", signature, jsonBody);
        } catch (Exception e) {
            LOGGER.error("[WowLibreClient] Error generating signature: {}", e.getMessage(), e);
            throw new InternalException("Failed to generate signature", transactionId);
        }
    }
}
