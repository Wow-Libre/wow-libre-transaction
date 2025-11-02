package com.wow.libre.infrastructure.repositories.plan;

import com.wow.libre.domain.port.out.plan.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaPlanAdapter implements ObtainPlan {
    private final PlanRepository planRepository;

    public JpaPlanAdapter(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<PlanEntity> findByStatusIsTrue(String transactionId) {
        return planRepository.findByStatusIsTrue();
    }
    
    @Override
    public List<PlanEntity> findByStatusIsTrueAndLanguage(String language, String transactionId) {
        return planRepository.findByStatusIsTrueAndLanguage(language);
    }
    
    @Override
    public Optional<PlanEntity> findById(Long planId, String transactionId) {
        return planRepository.findById(planId);
    }
}
