package com.wow.libre.application.services.wowlibre;

import ch.qos.logback.core.net.server.*;
import com.wow.libre.domain.dto.client.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.wowlibre.*;
import com.wow.libre.domain.port.out.client.*;
import com.wow.libre.infrastructure.client.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.cglib.core.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class WowLibreService implements WowLibrePort {

    public static final String WOWLIBRE = "WOWLIBRE";
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
        Optional<ClientEntity> client = obtainClient.findByUsername(WOWLIBRE, transactionId);
        ClientEntity tokens = new ClientEntity();

        if (client.isPresent()) {
            tokens = client.get();

            if (tokens.getExpirationDate().after(new Date())) {
                return tokens.getJwt();
            }

        }
        LoginResponse login = wowLibreClient.login(transactionId);
        tokens.setJwt(login.jwt);
        tokens.setExpirationDate(login.expirationDate);
        tokens.setRefreshToken(login.refreshToken);
        tokens.setUsername(WOWLIBRE);
        saveClient.save(tokens, transactionId);

        return login.jwt;
    }

    @Override
    public void sendPurchases(String jwt, Long serverId, Long userId, Long accountId, Double gold,
                              List<ItemQuantityModel> items, String reference, String transactionId) {
        wowLibreClient.sendPurchases(jwt, serverId, userId, accountId, gold, items, reference, transactionId);
    }
}
