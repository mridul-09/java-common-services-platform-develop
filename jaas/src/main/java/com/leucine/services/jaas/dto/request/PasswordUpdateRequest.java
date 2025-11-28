package com.leucine.services.jaas.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordUpdateRequest {
  private Long userId;

  @NotBlank(message = "Password cannot be empty")
  private String password;

  @NotBlank(message = "Confirm password cannot be empty")
  private String confirmPassword;

  @NotBlank(message = "Token cannot be empty")
  private String token;
}
