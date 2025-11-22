package com.wow.libre.domain.port.out.benefit_premium;

import com.wow.libre.infrastructure.entities.BenefitPremiumEntity;

public interface SaveBenefitPremium {
  void save(BenefitPremiumEntity benefitPremium);
}
