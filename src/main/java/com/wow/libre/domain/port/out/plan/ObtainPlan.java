package com.wow.libre.domain.port.out.plan;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainPlan {

    Optional<PlanEntity> findByStatusIsTrue(String transactionId);
}
