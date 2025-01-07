package com.wow.libre.infrastructure.client;

import com.wow.libre.domain.dto.client.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.shared.*;
import com.wow.libre.infrastructure.conf.*;
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

    public WowLibreClient(RestTemplate restTemplate, Configurations configurations) {
        this.restTemplate = restTemplate;
        this.configurations = configurations;
    }

    public LoginResponse login(String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        LoginRequest request = new LoginRequest(configurations.getLoginUsername(), configurations.getLoginPassword());


        headers.set(HEADER_TRANSACTION_ID, transactionId);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<GenericResponse<LoginResponse>> response = restTemplate.exchange(String.format("%s/api" +
                                    "/auth" +
                                    "/login",
                            configurations.getPathLoginWowLibre()),
                    HttpMethod.POST, entity,
                    new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[WowLibreClient] [login] Client/Server Error: {}. The request failed with a client or " +
                            "server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[WowLibreClient] [login]  Unexpected Error: {}. An unexpected error occurred during the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);

    }

    public GenericResponse<Void> sendPurchases(String jwt, Long serverId, Long userId, Long accountId, Double gold,
                                               List<ItemQuantityModel> items, String reference,
                                               String transactionId) {

        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<CreateTransactionItemsDto> entity = new HttpEntity<>(new CreateTransactionItemsDto(serverId,
                userId, accountId, reference, items, gold), headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/transaction/purchase",
                        configurations.getPathLoginWowLibre()))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url,
                    HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[WowLibreClient] [sendPurchases] Client/Server Error: {}. The request failed with a client " +
                            "or" +
                            " server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());

            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[WowLibreClient] [sendPurchases] Unexpected Error: {}. An unexpected error occurred during " +
                            "the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }
        throw new InternalException("Unexpected transaction failure", transactionId);

    }

    public GenericResponse<Void> sendBenefitsPremium(String jwt, SubscriptionBenefitsRequest request,
                                                     String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);


        HttpEntity<SubscriptionBenefitsRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/transaction/subscription-benefits",
                        configurations.getPathLoginWowLibre()))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [sendBenefitsPremium]  Client/Server Error: {}. Error with server " +
                            "client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [sendBenefitsPremium] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }
}
