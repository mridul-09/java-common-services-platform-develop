package com.leucine.services.jaas.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Payload for authentication request of user of type SSO
 * {
 * "username": "a0.01@mailinator.com",
 * "password": null,
 * "code": "u_Z3dRmtv0DTr_W2fcAjdMKDobEBWrZdjTkU9iXolng",
 * "state": "LOGIN"
 * }
 * "code" is used to get id token and access token from IDP (Identity Provider), code is one time use only
 *
 * <br> <br>
 * <p>
 * Payload for authentication request of user of type LOCAL
 * {
 * "username": "facility.admin.01",
 * "password": "JHVuRGF5MTIz"
 * }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuthenticationRequest {
  private String username;
  private String password;
  private String code;
  private String state;
  //For Client credential based authentication
  private String clientId;
  private String clientSecret;
  private String facilityId;
}
