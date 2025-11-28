package com.leucine.services.jaas.sso.azure;

import com.leucine.services.jaas.config.AzureADGraphConfig;
import lombok.Getter;

@Getter
public class AzureHandler {
  private final AzureOauth2Properties azureProperties;
  private final AzureADGraphConfig azureADGraphConfig;

  public AzureHandler(AzureOauth2Properties azureProperties, AzureADGraphConfig azureADGraphConfig) {
    this.azureProperties = azureProperties;
    this.azureADGraphConfig = azureADGraphConfig;
  }

}
