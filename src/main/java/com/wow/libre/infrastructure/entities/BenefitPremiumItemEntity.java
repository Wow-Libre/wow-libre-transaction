package com.wow.libre.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "benefit_premium_item")
public class BenefitPremiumItemEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "benefit_premium_id", referencedColumnName = "id", nullable = false)
  private BenefitPremiumEntity benefitPremium;
  
  @Column(name = "code", nullable = false)
  private String code;
  
  @Column(name = "quantity", nullable = false)
  private Integer quantity;
}

