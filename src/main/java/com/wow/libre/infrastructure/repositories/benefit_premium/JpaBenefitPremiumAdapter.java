package com.wow.libre.infrastructure.repositories.benefit_premium;

import com.wow.libre.domain.port.out.benefit_premium.ObtainBenefitPremium;
import com.wow.libre.domain.port.out.benefit_premium.SaveBenefitPremium;
import com.wow.libre.infrastructure.entities.BenefitPremiumEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaBenefitPremiumAdapter implements ObtainBenefitPremium, SaveBenefitPremium {
  private final BenefitPremiumRepository benefitPremiumRepository;

  public JpaBenefitPremiumAdapter(BenefitPremiumRepository benefitPremiumRepository) {
    this.benefitPremiumRepository = benefitPremiumRepository;
  }

  @Override
  public void save(BenefitPremiumEntity benefitPremium) {
    benefitPremiumRepository.save(benefitPremium);
  }

  @Override
  public List<BenefitPremiumEntity> findByStatusIsTrue() {
    return benefitPremiumRepository.findByStatusIsTrue();
  }

  @Override
  public List<BenefitPremiumEntity> findByRealmIdAndLanguageAndStatusIsTrue(Long realmId, String language, String transactionId) {
    return benefitPremiumRepository.findByRealmIdAndLanguageAndStatusIsTrue(realmId, language);
  }

  @Override
  public Optional<BenefitPremiumEntity> findById(Long id) {
    return benefitPremiumRepository.findById(id);
  }

  @Override
  public List<BenefitPremiumEntity> findByRealmIdAndStatusIsTrue(Long realmId) {
    return benefitPremiumRepository.findByRealmIdAndStatusIsTrue(realmId);
  }
}
