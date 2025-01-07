package com.wow.libre.application.services.wowlibre;

import com.wow.libre.domain.dto.client.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.wowlibre.*;
import com.wow.libre.domain.port.out.client.*;
import com.wow.libre.infrastructure.client.*;
import com.wow.libre.infrastructure.entities.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class WowLibreService implements WowLibrePort {
    public static final String CLIENT_DEFAULT_AUTH = "WOWLIBRE";
    private static final Logger LOGGER = LoggerFactory.getLogger(WowLibreService.class);
    private final WowLibreClient wowLibreClient;
    private final ObtainClient obtainClient;
    private final SaveClient saveClient;

    public WowLibreService(WowLibreClient wowLibreClient, ObtainClient obtainClient, SaveClient saveClient) {
        this.wowLibreClient = wowLibreClient;
        this.obtainClient = obtainClient;
        this.saveClient = saveClient;
    }


    @Override
    public String getJwt(String transactionId) {

        Optional<ClientEntity> client = obtainClient.findByUsername(CLIENT_DEFAULT_AUTH, transactionId);

        ClientEntity tokens = client.orElse(new ClientEntity());

        if (tokens.getExpirationDate().after(new Date())) {
            return tokens.getJwt();
        }


        try {
            LoginResponse login = wowLibreClient.login(transactionId);
            tokens.setJwt(login.jwt);
            tokens.setExpirationDate(login.expirationDate);
            tokens.setRefreshToken(login.refreshToken);
            tokens.setUsername(CLIENT_DEFAULT_AUTH);
            saveClient.save(tokens, transactionId);
        } catch (Exception e) {
            LOGGER.error("Invalid login WowLibre Platform {}", e.getLocalizedMessage());
            throw new InternalException("Invalid login WowLibre Platform", transactionId);
        }

        return tokens.getJwt();
    }

    @Override
    public void sendPurchases(String jwt, Long serverId, Long userId, Long accountId, Double gold,
                              List<ItemQuantityModel> items, String reference, String transactionId) {
        wowLibreClient.sendPurchases(jwt, serverId, userId, accountId, gold, items, reference, transactionId);
    }

    @Override
    public void sendBenefitsPremium(String jwt, Long serverId, Long userId, Long accountId,
                                    Long characterId,
                                    List<ItemQuantityModel> items, String benefitType, Double amount,
                                    String transactionId) {
        wowLibreClient.sendBenefitsPremium(jwt, new SubscriptionBenefitsRequest(serverId, userId, accountId,
                        characterId, items, benefitType, amount),
                transactionId);
    }
}
