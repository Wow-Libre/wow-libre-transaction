package com.wow.libre.infrastructure.client;

import com.wow.libre.domain.dto.payu.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

@Component
public class PayUClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayUClient.class);

    private final RestTemplate restTemplate;

    public PayUClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PayUOrderDetailResponse getOrderDetailByReference(String host, String referenceCode, String apiLogin,
                                                             String apiKey) {
        try {
            LOGGER.info("Getting order detail for reference code: {}", referenceCode);

            PayUOrderDetailRequest request = PayUOrderDetailRequest.builder()
                    .language("es")
                    .command("ORDER_DETAIL_BY_REFERENCE_CODE")
                    .merchant(PayUOrderDetailRequest.PayUMerchant.builder()
                            .apiLogin(apiLogin)
                            .apiKey(apiKey)
                            .build())
                    .details(PayUOrderDetailRequest.PayUDetails.builder()
                            .referenceCode(referenceCode)
                            .build())
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");

            HttpEntity<PayUOrderDetailRequest> entity = new HttpEntity<>(request, headers);

            PayUOrderDetailResponse response = restTemplate.postForObject(
                    "https://sandbox.api.payulatam.com/reports-api/4.0/service.cgi",
                    entity,
                    PayUOrderDetailResponse.class);

            LOGGER.info("Successfully retrieved order detail for reference code: {}", referenceCode);
            return response;

        } catch (RestClientException e) {
            LOGGER.error("Error getting order detail for reference code: {}", referenceCode, e);
            throw new RuntimeException("Error communicating with PayU API", e);
        }
    }
}
