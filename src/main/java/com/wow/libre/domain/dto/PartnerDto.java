package com.wow.libre.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class PartnerDto {
    @NotNull
    private String name;
    @NotNull
    private Long realmId;
}
