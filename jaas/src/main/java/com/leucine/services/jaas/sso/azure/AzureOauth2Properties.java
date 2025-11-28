package com.leucine.services.jaas.sso.azure;

import com.leucine.services.jaas.sso.Oauth2Properties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AzureOauth2Properties extends Oauth2Properties {
  private String clientSecret;
  /*
    {
        "name": "displayName",
        "email": "mail",
        "lastName": "surname",
        "username": "userPrincipalName",
        "firstName": "givenName",
        "department": "department",
        "employeeId": "onPremisesSamAccountName"
    }
    These mappings are used to map the user attributes from the response of the identity provider to the user attributes in the database.
   */
  private String tenantId;
}
