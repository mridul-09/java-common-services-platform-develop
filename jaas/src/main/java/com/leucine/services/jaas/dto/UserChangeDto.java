package com.leucine.services.jaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChangeDto {
  private String field;
  private String oldValue;
  private String newValue;
}
