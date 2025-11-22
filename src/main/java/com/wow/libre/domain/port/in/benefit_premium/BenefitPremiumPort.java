package com.wow.libre.domain.port.in.benefit_premium;

import com.wow.libre.domain.dto.BenefitsPremiumDto;
import com.wow.libre.domain.dto.CreateBenefitPremiumDto;
import java.util.List;

public interface BenefitPremiumPort {
  void createBenefitPremium(CreateBenefitPremiumDto request, String transactionId);

  void deleteBenefitPremium(Long id, String transactionId);

  List<BenefitsPremiumDto> findByLanguageAndRealmId(String language, Long realmId, String transactionId);

  List<BenefitsPremiumDto> findByRealmIdStatusIsTrue(Long realmId, String transactionId);
}
