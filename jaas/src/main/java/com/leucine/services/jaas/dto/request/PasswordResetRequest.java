package com.leucine.services.jaas.dto.request;

import lombok.Data;

@Data
public class PasswordResetRequest {
  private String email;
}
