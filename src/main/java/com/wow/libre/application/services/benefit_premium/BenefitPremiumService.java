package com.wow.libre.application.services.benefit_premium;

import com.wow.libre.domain.dto.BenefitPremiumItemDto;
import com.wow.libre.domain.dto.BenefitsPremiumDto;
import com.wow.libre.domain.dto.CreateBenefitPremiumDto;
import com.wow.libre.domain.dto.UpdateBenefitPremiumDto;
import com.wow.libre.domain.exception.InternalException;
import com.wow.libre.domain.port.in.benefit_premium.BenefitPremiumPort;
import com.wow.libre.domain.port.out.benefit_premium.ObtainBenefitPremium;
import com.wow.libre.domain.port.out.benefit_premium.SaveBenefitPremium;
import com.wow.libre.infrastructure.entities.BenefitPremiumEntity;
import com.wow.libre.infrastructure.entities.BenefitPremiumItemEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BenefitPremiumService implements BenefitPremiumPort {

  private final ObtainBenefitPremium obtainBenefitPremium;
  private final SaveBenefitPremium saveBenefitPremium;

  public BenefitPremiumService(ObtainBenefitPremium obtainBenefitPremium, SaveBenefitPremium saveBenefitPremium) {
    this.obtainBenefitPremium = obtainBenefitPremium;
    this.saveBenefitPremium = saveBenefitPremium;
  }

  @Override
  public void createBenefitPremium(CreateBenefitPremiumDto request, String transactionId) {
    BenefitPremiumEntity benefitPremiumEntity = new BenefitPremiumEntity();
    benefitPremiumEntity.setImg(request.getImg());
    benefitPremiumEntity.setName(request.getName());
    benefitPremiumEntity.setDescription(request.getDescription());
    benefitPremiumEntity.setCommand(request.getCommand());
    benefitPremiumEntity.setSendItem(request.isSendItem());
    benefitPremiumEntity.setReactivable(request.isReactivable());
    benefitPremiumEntity.setBtnText(request.getBtnText());
    benefitPremiumEntity.setType(request.getType());
    benefitPremiumEntity.setRealmId(request.getRealmId());
    benefitPremiumEntity.setLanguage(request.getLanguage());
    benefitPremiumEntity.setStatus(true);

    // Guardar items si existen
    if (request.getItems() != null && !request.getItems().isEmpty()) {
      List<BenefitPremiumItemEntity> items = new ArrayList<>();
      for (BenefitPremiumItemDto itemDto : request.getItems()) {
        BenefitPremiumItemEntity itemEntity = new BenefitPremiumItemEntity();
        itemEntity.setCode(itemDto.getCode());
        itemEntity.setQuantity(itemDto.getQuantity());
        itemEntity.setBenefitPremium(benefitPremiumEntity);
        items.add(itemEntity);
      }
      benefitPremiumEntity.setItems(items);
    }

    saveBenefitPremium.save(benefitPremiumEntity);
  }

  @Override
  public void deleteBenefitPremium(Long id, String transactionId) {
    Optional<BenefitPremiumEntity> benefitPremium = obtainBenefitPremium.findById(id);

    if (benefitPremium.isEmpty()) {
      throw new InternalException("Benefit Not found", transactionId);
    }
    BenefitPremiumEntity benefitPremiumEntity = benefitPremium.get();
    benefitPremiumEntity.setStatus(false);
    saveBenefitPremium.save(benefitPremiumEntity);
  }

  @Override
  public void updateBenefitPremium(UpdateBenefitPremiumDto request, String transactionId) {

    Optional<BenefitPremiumEntity> benefitPremium = obtainBenefitPremium.findById(request.getId());

    if (benefitPremium.isEmpty()) {
      throw new InternalException("Benefit Not found", transactionId);
    }

    BenefitPremiumEntity benefitPremiumEntity = benefitPremium.get();
    benefitPremiumEntity.setImg(request.getImg() != null ? request.getImg() : benefitPremiumEntity.getImg());
    benefitPremiumEntity.setName(request.getName() != null ? request.getName() : benefitPremiumEntity.getName());
    benefitPremiumEntity.setDescription(
        request.getDescription() != null ? request.getDescription() : benefitPremiumEntity.getDescription());
    benefitPremiumEntity.setCommand(request.getCommand() != null ? request.getCommand() : benefitPremiumEntity.getCommand());
    benefitPremiumEntity.setSendItem(request.getSendItem() != null ? request.getSendItem() : benefitPremiumEntity.isSendItem());
    benefitPremiumEntity.setReactivable(request.getReactivable() != null ? request.getReactivable() : benefitPremiumEntity.isReactivable());
    benefitPremiumEntity.setBtnText(request.getBtnText() != null ? request.getBtnText() : benefitPremiumEntity.getBtnText());
    benefitPremiumEntity.setType(request.getType() != null ? request.getType() : benefitPremiumEntity.getType());
    benefitPremiumEntity.setRealmId(request.getRealmId() != null ? request.getRealmId() : benefitPremiumEntity.getRealmId());
    benefitPremiumEntity.setLanguage(request.getLanguage() != null ? request.getLanguage() : benefitPremiumEntity.getLanguage());

    // Actualizar items si se proporcionan
    if (request.getItems() != null) {
      // Eliminar items existentes
      if (benefitPremiumEntity.getItems() != null) {
        benefitPremiumEntity.getItems().clear();
      } else {
        benefitPremiumEntity.setItems(new ArrayList<>());
      }

      // Agregar nuevos items
      for (BenefitPremiumItemDto itemDto : request.getItems()) {
        BenefitPremiumItemEntity itemEntity = new BenefitPremiumItemEntity();
        itemEntity.setCode(itemDto.getCode());
        itemEntity.setQuantity(itemDto.getQuantity());
        itemEntity.setBenefitPremium(benefitPremiumEntity);
        benefitPremiumEntity.getItems().add(itemEntity);
      }
    }

    saveBenefitPremium.save(benefitPremiumEntity);
  }

  @Override
  public List<BenefitsPremiumDto> findByLanguageAndRealmId(String language, Long realmId, String transactionId) {
    return obtainBenefitPremium.findByRealmIdAndLanguageAndStatusIsTrue(realmId, language, transactionId).stream().map(this::mapToModel)
        .toList();
  }

  private BenefitsPremiumDto mapToModel(BenefitPremiumEntity benefitPremiumEntity) {
    List<BenefitPremiumItemDto> items = new ArrayList<>();
    if (benefitPremiumEntity.getItems() != null) {
      items = benefitPremiumEntity.getItems().stream()
          .map(item -> BenefitPremiumItemDto.builder()
              .code(item.getCode())
              .quantity(item.getQuantity())
              .build())
          .toList();
    }

    return BenefitsPremiumDto.builder()
        .id(benefitPremiumEntity.getId())
        .name(benefitPremiumEntity.getName())
        .description(benefitPremiumEntity.getDescription())
        .img(benefitPremiumEntity.getImg())
        .command(benefitPremiumEntity.getCommand())
        .sendItem(benefitPremiumEntity.isSendItem())
        .reactivable(benefitPremiumEntity.isReactivable())
        .btnText(benefitPremiumEntity.getBtnText())
        .type(benefitPremiumEntity.getType())
        .realmId(benefitPremiumEntity.getRealmId())
        .language(benefitPremiumEntity.getLanguage())
        .status(benefitPremiumEntity.isStatus())
        .items(items)
        .build();
  }
}
