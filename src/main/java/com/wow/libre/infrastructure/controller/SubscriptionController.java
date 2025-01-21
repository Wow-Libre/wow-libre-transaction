package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.subscription.*;
import com.wow.libre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.wow.libre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {
    private final SubscriptionPort subscriptionPort;

    public SubscriptionController(SubscriptionPort subscriptionPort) {
        this.subscriptionPort = subscriptionPort;
    }

    @GetMapping("/pill-home")
    public ResponseEntity<GenericResponse<PillWidgetHomeDto>> pillHomeNotAuthenticated(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        PillWidgetHomeDto accounts = subscriptionPort.getPillHome(null, locale.getLanguage(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(accounts, transactionId).created().build());
    }

    @GetMapping("/pill-user")
    public ResponseEntity<GenericResponse<PillWidgetHomeDto>> pillHomeAuthenticated(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        PillWidgetHomeDto accounts = subscriptionPort.getPillHome(userId, locale.getLanguage(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(accounts, transactionId).created().build());
    }

    @GetMapping
    public ResponseEntity<GenericResponse<Boolean>> activeSubscription(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId) {

        Boolean subscriptionActive = subscriptionPort.isActiveSubscription(userId, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(subscriptionActive, transactionId).created().build());
    }

    @GetMapping("/benefits")
    public ResponseEntity<GenericResponse<SubscriptionBenefitsDto>> benefitsSubscription(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale,
            @RequestParam(name = "server_id") final Long serverId) {

        SubscriptionBenefitsDto subscriptionActive = subscriptionPort.benefits(userId, serverId, locale.getLanguage(),
                transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(subscriptionActive, transactionId).created().build());
    }

    @PostMapping("/claim-benefits")
    public ResponseEntity<GenericResponse<Void>> claimBenefitsSubscription(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale,
            @RequestBody @Valid ClaimSubscriptionBenefitsDto req) {

        subscriptionPort.claimBenefits(req.getServerId(), userId, req.getAccountId(), req.getCharacterId(),
                locale.getLanguage(), req.getBenefitId(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }


}
