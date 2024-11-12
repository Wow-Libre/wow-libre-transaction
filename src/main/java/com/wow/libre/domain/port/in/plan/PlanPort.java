package com.wow.libre.domain.port.in.plan;

import com.wow.libre.domain.dto.*;

public interface PlanPort {
    PlanDetailDto getPlan(String transactionId);
}
