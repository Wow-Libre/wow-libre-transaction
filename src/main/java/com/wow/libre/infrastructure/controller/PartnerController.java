package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.partner.*;
import com.wow.libre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.wow.libre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/partner")
public class PartnerController {
    private final PartnerPort partnerPort;

    public PartnerController(PartnerPort partnerPort) {
        this.partnerPort = partnerPort;
    }

    @GetMapping("/exist/{realmId}")
    public ResponseEntity<GenericResponse<Boolean>> getPartnerByRealmId(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable Long realmId) {

        boolean existPartner = partnerPort.exists(realmId, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(existPartner, transactionId).ok().build());
    }


    @PostMapping
    public ResponseEntity<GenericResponse<Void>> createPartner(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid PartnerDto partnerDto) {

        partnerPort.create(partnerDto.getName(), partnerDto.getRealmId(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }
}
