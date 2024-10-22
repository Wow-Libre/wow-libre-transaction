package com.wow.libre.domain.shared;

import lombok.*;

import java.util.*;
@Data
public class NotNullValuesDto {
  private Integer numberOfInvalid;
  private List<String> valuesInvalid;
}
