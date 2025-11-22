package com.wow.libre.domain.port.out.benefit_premium;

import com.wow.libre.infrastructure.entities.BenefitPremiumEntity;
import java.util.List;
import java.util.Optional;

public interface ObtainBenefitPremium {
  List<BenefitPremiumEntity> findByStatusIsTrue();

  List<BenefitPremiumEntity> findByRealmIdAndLanguageAndStatusIsTrue(Long realmId, String language, String transactionId);

  Optional<BenefitPremiumEntity> findById(Long id);

  List<BenefitPremiumEntity> findByRealmIdAndStatusIsTrue(Long realmId);
}
