package com.wow.libre.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BenefitPremiumType {
    CHANGE_FACTION("CHANGE_FACTION"),
    CHANGE_RACE("CHANGE_RACE"),
    CUSTOMIZE("CUSTOMIZE"),
    ITEM("ITEM");

    @JsonValue
    private final String type;

    @JsonCreator
    public static BenefitPremiumType fromString(String type) {
        return Arrays.stream(values())
                .filter(benefitType -> benefitType.type.equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid benefit premium type: " + type));
    }

    public static BenefitPremiumType getType(String type) {
        return fromString(type);
    }
}

