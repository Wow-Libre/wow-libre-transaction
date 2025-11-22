package com.wow.libre.infrastructure.repositories.benefit_premium;

import com.wow.libre.infrastructure.entities.BenefitPremiumEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BenefitPremiumRepository extends CrudRepository<BenefitPremiumEntity, Long> {
  List<BenefitPremiumEntity> findByRealmIdAndLanguageAndStatusIsTrue(Long realmId, String language);

  List<BenefitPremiumEntity> findByStatusIsTrue();

}
