package com.leucine.services.jaas.controller.impl;

import com.auth0.jwk.JwkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.controller.IAuthController;
import com.leucine.services.jaas.dto.*;
import com.leucine.services.jaas.dto.request.*;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.exception.JaasException;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import com.leucine.services.jaas.service.IAuthService;
import com.leucine.services.jaas.util.Utility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
@AllArgsConstructor
public class AuthController implements IAuthController {

  private final IAuthService service;

  @Override
  public Response<AuthenticationDto> register(UserRegistrationRequest userRegistrationRequest, String serviceId) throws MalformedURLException, JwkException, JsonProcessingException {
    try {
      userRegistrationRequest.setPassword(new String(Base64.getDecoder().decode(userRegistrationRequest.getPassword())));
      userRegistrationRequest.setConfirmPassword(new String(Base64.getDecoder().decode(userRegistrationRequest.getConfirmPassword())));
    } catch (IllegalArgumentException ex) {
      throw new JaasException(ErrorCode.INVALID_ENCRYPTION);
    }
    return Response.builder().data(service.register(userRegistrationRequest, serviceId)).build();
  }

  @Override
  public Response<AuthenticationDto> authenticate(AuthenticationRequest authenticationRequest, String serviceId) throws MalformedURLException, JwkException, JsonProcessingException {
    try {
      if (!Utility.isEmpty(authenticationRequest.getPassword())) {
        authenticationRequest.setPassword(new String(Base64.getDecoder().decode(authenticationRequest.getPassword())));
      } else if (!Utility.isEmpty(authenticationRequest.getClientSecret())) {
        authenticationRequest.setClientSecret(new String(Base64.getDecoder().decode(authenticationRequest.getClientSecret())));
      }
    } catch (IllegalArgumentException ex) {
      throw new JaasException(ErrorCode.INVALID_ENCRYPTION);
    }
    return Response.builder().data(service.authenticate(authenticationRequest, serviceId)).build();
  }

  @Override
  public Response<AuthenticationDto> relogin(ReloginRequest reloginRequest, String serviceId) throws MalformedURLException, JwkException, JsonProcessingException {
    try {
      if (!Utility.isEmpty(reloginRequest.getPassword())) {
        reloginRequest.setPassword(new String(Base64.getDecoder().decode(reloginRequest.getPassword())));
      }
    } catch (IllegalArgumentException ex) {
      throw new JaasException(ErrorCode.INVALID_ENCRYPTION);
    }
    return Response.builder().data(service.relogin(reloginRequest, serviceId)).build();
  }

  @Override
  public Response<AuthenticationDto> logout(LogoutRequest logoutRequest) throws JsonProcessingException {
    return Response.builder().data(service.logout(logoutRequest)).build();
  }

  @Override
  public PrincipalUser authorize(AuthorizationRequest authorizationRequest) {
    return service.authorize(authorizationRequest);
  }

  @Override
  public Response<ValidateIdentityDto> validateIdentity(ValidateIdentityRequest validateIdentityRequest) {
    return Response.builder().data(service.validateIdentity(validateIdentityRequest)).build();
  }

  @Override
  public Response<BasicDto> resetToken(GeneratePasswordResetTokenRequest generatePasswordResetTokenRequest) {
    return Response.builder().data(service.resetToken(generatePasswordResetTokenRequest)).build();
  }

  @Override
  public Response<BasicDto> notifyAdmin(NotifyAdminRequest notifyAdminRequest, String serviceId) {
    return Response.builder().data(service.notifyAdmin(notifyAdminRequest, serviceId)).build();
  }

  @Override
  public Response<TokenDto> validateChallengeQuestionsAnswer(ChallengeQuestionsAnswerRequest challengeQuestionsAnswerRequest) {
    return Response.builder().data(service.validateChallengeQuestionsAnswer(challengeQuestionsAnswerRequest)).build();
  }


  @Override
  public Response<TokenDto> validateCredentials(ValidateCredentialsRequest validateCredentialsRequest, String serviceId) throws IOException, JwkException, NoSuchAlgorithmException, InvalidKeySpecException {
    try {
      if (!Utility.isEmpty(validateCredentialsRequest.getPassword())) {
        validateCredentialsRequest.setPassword(new String(Base64.getDecoder().decode(validateCredentialsRequest.getPassword())));
      }
    } catch (IllegalArgumentException ex) {
      throw new JaasException(ErrorCode.INVALID_ENCRYPTION);
    }

    if (Utility.isEmpty(validateCredentialsRequest.getPassword()) && Utility.isEmpty(validateCredentialsRequest.getCode())) {
      throw new JaasException(ErrorCode.INVALID_CREDENTIALS);
    }

    if (validateCredentialsRequest.getPurpose() == null) {
      validateCredentialsRequest.setPurpose(Misc.ValidationPurpose.NA);
    }
    return Response.builder().data(service.validateCredentials(validateCredentialsRequest, serviceId)).build();
  }

  @Override
  public Response<AdditionalVerificationDto> additionalVerification(@RequestBody AdditionalVerificationRequest additionalVerificationRequest) {
    return Response.builder().data(service.additionalVerification(additionalVerificationRequest)).build();
  }

  @Override
  public Response<TokenDto> validateToken(ValidateTokenRequest validateTokenRequest) {
    return Response.builder().data(service.validateToken(validateTokenRequest)).build();
  }

  @Override
  public Response<TokenDto> refreshToken(RefreshTokenRequest refreshTokenRequest) {
    return Response.builder().data(service.refreshToken(refreshTokenRequest)).build();
  }

  @Override
  public Response<RedirectUrlDto> getSSORedirectUrl(SSORedirectDto ssoRedirectDto) throws UnsupportedEncodingException, JsonProcessingException {
    return Response.builder().data(service.getSSORedirectUrl(ssoRedirectDto)).build();
  }

  @Override
  public Response<AuthExtras> getExtras(String fqdn) throws JsonProcessingException {
    return Response.builder().data(service.getExtras(fqdn)).build();
  }

  @Override
  public Response<AccountLookupDto> accountLookup(String username) throws JsonProcessingException, UnsupportedEncodingException {
    AccountLookupDto accountLookupDto = service.accountLookup(username);
    return Response.builder().data(accountLookupDto).build();
  }

  @Override
  public Response<BasicDto> updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
    try {
      passwordUpdateRequest.setPassword(new String(Base64.getDecoder().decode(passwordUpdateRequest.getPassword())));
      passwordUpdateRequest.setConfirmPassword(new String(Base64.getDecoder().decode(passwordUpdateRequest.getConfirmPassword())));
    } catch (IllegalArgumentException ex) {
      throw new JaasException(ErrorCode.INVALID_ENCRYPTION);
    }
    return Response.builder().data(service.updatePassword(passwordUpdateRequest)).build();
  }
}
