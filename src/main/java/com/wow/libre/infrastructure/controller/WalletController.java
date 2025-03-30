package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.port.in.wallet.*;
import com.wow.libre.domain.shared.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.wow.libre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final WalletPort walletPort;

    public WalletController(WalletPort walletPort) {
        this.walletPort = walletPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<Long>> wallet(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId) {

        final Long points = walletPort.getPoints(userId, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(points, transactionId).ok().build());
    }


}
