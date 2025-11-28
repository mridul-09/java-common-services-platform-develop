package com.leucine.services.jaas.dto.request;

import lombok.Data;

@Data
public class AuthorizationRequest {
  private String path;
  private String method;
}