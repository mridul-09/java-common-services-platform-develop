package com.leucine.services.jaas.sso.okta;

import com.leucine.services.jaas.constant.Oauth2;
import com.okta.sdk.client.AuthorizationMode;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.api.UserApi;
import com.okta.sdk.resource.client.ApiClient;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Getter
public class OktaHandler {
  private final OktaOAuth2Properties oktaProperties;

  public OktaHandler(OktaOAuth2Properties oktaProperties) {
    this.oktaProperties = oktaProperties;
  }

  public UserApi getUserApi() {
    return new UserApi(getApiClient());
  }

  private ApiClient getApiClient() {
    Path path = Paths.get(oktaProperties.getPrivateKeyPath());

    return Clients.builder()
      .setOrgUrl(oktaProperties.getOrgUrl())
      .setAuthorizationMode(AuthorizationMode.PRIVATE_KEY)
      .setClientId(oktaProperties.getApiClientId())
      .setScopes(Set.of(Oauth2.Okta.OKTA_USER_READ_SCOPE))
      .setPrivateKey(path)
      .build();
  }

}
