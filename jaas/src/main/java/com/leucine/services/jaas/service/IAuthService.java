package com.leucine.services.jaas.service;

import com.auth0.jwk.JwkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.leucine.services.jaas.dto.*;
import com.leucine.services.jaas.dto.request.*;
import com.leucine.services.jaas.model.helper.PrincipalUser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IAuthService {
  AuthenticationDto register(UserRegistrationRequest userRegistrationRequest, String serviceId) throws MalformedURLException, JwkException, JsonProcessingException;

  AuthenticationDto authenticate(AuthenticationRequest authenticationRequest, String serviceId) throws MalformedURLException, JwkException, JsonProcessingException;

  AuthenticationDto relogin(ReloginRequest reloginRequest, String serviceId) throws MalformedURLException, JwkException, JsonProcessingException;

  AuthenticationDto logout(LogoutRequest logoutRequest) throws JsonProcessingException;

  TokenDto validateCredentials(ValidateCredentialsRequest validateCredentialsRequest, String serviceId) throws IOException, JwkException, NoSuchAlgorithmException, InvalidKeySpecException;

  AdditionalVerificationDto additionalVerification(AdditionalVerificationRequest additionalVerificationRequest);

  TokenDto validateToken(ValidateTokenRequest validateTokenRequest);

  TokenDto refreshToken(RefreshTokenRequest refreshTokenRequest);

  PrincipalUser authorize(AuthorizationRequest authorizationRequest);

  ValidateIdentityDto validateIdentity(ValidateIdentityRequest validateIdentityRequest);

  BasicDto resetToken(GeneratePasswordResetTokenRequest generatePasswordResetTokenRequest);

  BasicDto notifyAdmin(NotifyAdminRequest notifyAdminRequest, String serviceId);

  TokenDto validateChallengeQuestionsAnswer(ChallengeQuestionsAnswerRequest challengeQuestionsAnswerRequest);

  BasicDto updatePassword(PasswordUpdateRequest passwordUpdateRequest);

  AccountLookupDto accountLookup(String username) throws JsonProcessingException, UnsupportedEncodingException;

  AuthExtras getExtras(String fqdn) throws JsonProcessingException;

  RedirectUrlDto getSSORedirectUrl(SSORedirectDto ssoRedirectDto) throws UnsupportedEncodingException, JsonProcessingException;

}
