package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.plan.*;
import com.wow.libre.domain.shared.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.wow.libre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanPort planPort;

    public PlanController(PlanPort planPort) {
        this.planPort = planPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<PlanDetailDto>> planAvailable(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {

        final PlanDetailDto plan = planPort.getPlan(transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(plan, transactionId).ok().build());
    }

}
