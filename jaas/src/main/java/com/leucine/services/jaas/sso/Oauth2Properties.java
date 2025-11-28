package com.leucine.services.jaas.sso;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2Properties {
  private String clientId;
  private String redirectUri;
  private String issuer;
  private String privateKeyPath;
  private String postLogoutRedirectUri;
  private OAuthUserDto mappings;
  private String prompt;
}
