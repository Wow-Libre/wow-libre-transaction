package com.wow.libre.infrastructure.entities;

import com.wow.libre.domain.enums.BenefitPremiumType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "benefit_premium")
public class BenefitPremiumEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  private String img;
  private String name;
  private String description;
  private String command;
  @Column(name = "send_item")
  private boolean sendItem;
  private boolean reactivable;
  @Column(name = "btn_text")
  private String btnText;
  @Enumerated(EnumType.STRING)
  private BenefitPremiumType type;
  @Column(name = "realm_id")
  private Long realmId;
  private String language;
  private Double amount;
  private boolean status;

  @OneToMany(mappedBy = "benefitPremium", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<BenefitPremiumItemEntity> items;
}
