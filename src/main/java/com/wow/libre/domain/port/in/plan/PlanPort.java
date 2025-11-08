package com.wow.libre.domain.port.in.plan;

import com.wow.libre.domain.dto.*;

import java.util.List;

public interface PlanPort {
    List<PlanDetailDto> getPlan(String language, String transactionId);
}
