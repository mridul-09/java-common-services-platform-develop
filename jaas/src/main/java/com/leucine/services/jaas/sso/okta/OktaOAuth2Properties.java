package com.leucine.services.jaas.sso.okta;

import com.leucine.services.jaas.sso.Oauth2Properties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OktaOAuth2Properties extends Oauth2Properties {
  private String clientSecret;
  private String apiClientId;
  private String orgUrl;
}
