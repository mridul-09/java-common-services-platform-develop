package com.leucine.services.jaas.sso;

import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.sso.azure.AzureHandler;
import com.leucine.services.jaas.sso.azure.AzureProvider;
import com.leucine.services.jaas.sso.okta.OktaHandler;
import com.leucine.services.jaas.sso.okta.OktaProvider;

public class OAuth2ProviderFactory {
  private final OktaHandler oktaHandler;
  private final AzureHandler azureHandler;

  public OAuth2ProviderFactory(OktaHandler oktaHandler, AzureHandler azureHandler) {
    this.oktaHandler = oktaHandler;
    this.azureHandler = azureHandler;
  }

  public IOauth2Provider getOAuth2Provider(Misc.UserType provider) {
    return switch (provider) {
      case OKTA -> new OktaProvider(oktaHandler);
      case AZURE -> new AzureProvider(azureHandler);
      default -> throw new IllegalArgumentException("Invalid provider: " + provider);
    };
  }
}
