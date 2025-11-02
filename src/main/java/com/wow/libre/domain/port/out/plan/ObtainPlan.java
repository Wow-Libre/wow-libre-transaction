package com.wow.libre.domain.port.out.plan;

import com.wow.libre.infrastructure.entities.*;

import java.util.*;

public interface ObtainPlan {

    List<PlanEntity> findByStatusIsTrue(String transactionId);
    
    List<PlanEntity> findByStatusIsTrueAndLanguage(String language, String transactionId);
    
    Optional<PlanEntity> findById(Long planId, String transactionId);
}
