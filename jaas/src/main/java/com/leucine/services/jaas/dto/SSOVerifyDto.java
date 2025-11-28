package com.leucine.services.jaas.dto;

import lombok.Data;

@Data
public class SSOVerifyDto {
  private String clientId;
  private String tenantId;
  private String secretId;
}
