package com.leucine.services.jaas.dto.request;

import com.leucine.services.jaas.constant.Type;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdditionalVerificationRequest {
  @NotBlank(message = "identifier cannot be empty")
  private String identifier;

  @NotBlank(message = "type cannot be empty")
  private Type.AdditionalVerification type;

  @NotBlank(message = "token cannot be empty")
  private String token;
}