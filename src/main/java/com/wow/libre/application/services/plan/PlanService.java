package com.wow.libre.application.services.plan;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.plan.*;
import com.wow.libre.domain.port.out.plan.*;
import org.springframework.stereotype.*;

@Service
public class PlanService implements PlanPort {

    private final ObtainPlan obtainPlan;

    public PlanService(ObtainPlan obtainPlan) {
        this.obtainPlan = obtainPlan;
    }

    @Override
    public PlanDetailDto getPlan(String transactionId) {
        return obtainPlan.findByStatusIsTrue(transactionId)
                .map(plan -> {
                    double discountPercentage = plan.getDiscount() / 100.0; // Convierte el descuento entero a
                    // porcentaje
                    double discountedPrice = plan.getPrice() * (1 - discountPercentage); // Calcula el precio con
                    // descuento
                    return new PlanDetailDto(
                            plan.getName(),
                            plan.getPrice(),
                            discountedPrice,
                            plan.getDiscount(),
                            plan.isStatus()
                    );
                })
                .orElse(null);

    }
}
