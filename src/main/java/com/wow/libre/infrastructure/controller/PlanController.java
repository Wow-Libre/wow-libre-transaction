package com.wow.libre.infrastructure.controller;

import static com.wow.libre.domain.constant.Constants.HEADER_TRANSACTION_ID;

import com.wow.libre.domain.dto.PlanDetailDto;
import com.wow.libre.domain.port.in.plan.PlanPort;
import com.wow.libre.domain.shared.GenericResponse;
import com.wow.libre.domain.shared.GenericResponseBuilder;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plan")
public class PlanController {
  private final PlanPort planPort;

  public PlanController(PlanPort planPort) {
    this.planPort = planPort;
  }

  @GetMapping
  public ResponseEntity<GenericResponse<List<PlanDetailDto>>> planAvailable(
      @RequestParam(name = "language") String language,
      @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {

    final List<PlanDetailDto> plans = planPort.getPlan(language, transactionId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(new GenericResponseBuilder<>(plans, transactionId).ok().build());
  }

}
