package com.leucine.services.jaas.dto.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
  private String accessToken;
  private String refreshToken;
}