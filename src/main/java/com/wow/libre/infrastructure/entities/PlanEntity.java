package com.wow.libre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Data
@Entity
@Table(name = "plan")
public class PlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private Double price;
    private Integer discount;
    private boolean status;
    private String currency;
    @Column(name = "frequency_type")
    private String frequencyType;
    @Column(name = "frequency_value")
    private Integer frequencyValue;
    @Column(name = "free_trial_days")
    private Integer freeTrialDays;
    @Column(name = "tax")
    private String tax;
    @Column(name = "return_tax")
    private String returnTax;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
