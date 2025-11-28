package com.leucine.services.jaas.dto.request;

import com.leucine.services.jaas.constant.Type;
import lombok.Data;

@Data
public class ValidateTokenRequest {
  private String token;
  private Type.Token type;
}