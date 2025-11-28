package com.leucine.services.jaas.dto.request;

import lombok.Data;

@Data
public class ReloginRequest {
  private String username;
  private String password;
  private String code;
  private String accessToken;
  private String state;
}
