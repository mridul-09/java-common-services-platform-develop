package com.leucine.services.jaas.controller;

import com.auth0.jwk.JwkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.dto.*;
import com.leucine.services.jaas.dto.request.*;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/v1/auth")
public interface IAuthController {

  @PatchMapping("/register")
  Response<AuthenticationDto> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest, @RequestAttribute(Misc.INTERCEPTOR_SERVICES_ID) String serviceId) throws MalformedURLException, JwkException, JsonProcessingException;

  //TODO: This won't work for AZURE SSO. Need to implement it
  @PostMapping("/login")
  @ResponseBody
  Response<AuthenticationDto> authenticate(@RequestBody AuthenticationRequest authenticationRequest, @RequestAttribute(Misc.INTERCEPTOR_SERVICES_ID) String serviceId) throws MalformedURLException, JwkException, JsonProcessingException;

  @PostMapping("/re-login")
  @ResponseBody
  Response<AuthenticationDto> relogin(@RequestBody ReloginRequest reloginRequest, @RequestAttribute(Misc.INTERCEPTOR_SERVICES_ID) String serviceId) throws MalformedURLException, JwkException, JsonProcessingException;

  @PostMapping("/logout")
  @ResponseBody
  Response<AuthenticationDto> logout(@RequestBody LogoutRequest logoutRequest) throws JsonProcessingException;

  @PatchMapping("/authorize")
  PrincipalUser authorize(@RequestBody AuthorizationRequest authorizationRequest);

  @PatchMapping("/credentials/validate")
  Response<TokenDto> validateCredentials(@RequestBody ValidateCredentialsRequest validateCredentialsRequest, @RequestAttribute(Misc.INTERCEPTOR_SERVICES_ID) String serviceId) throws IOException, JwkException, NoSuchAlgorithmException, InvalidKeySpecException;

  @PatchMapping("/additional/verification")
  Response<AdditionalVerificationDto> additionalVerification(@RequestBody AdditionalVerificationRequest additionalVerificationRequest);

  @PatchMapping("/token/validate")
  Response<TokenDto> validateToken(@RequestBody ValidateTokenRequest validateTokenRequest);

  @PostMapping("/token/refresh")
  Response<TokenDto> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest);

  @PatchMapping("/identity/validate")
  Response<ValidateIdentityDto> validateIdentity(@RequestBody ValidateIdentityRequest validateIdentityRequest);

  @PatchMapping("/token/reset")
  Response<BasicDto> resetToken(@RequestBody GeneratePasswordResetTokenRequest generatePasswordResetTokenRequest);

  @PatchMapping("/admin/notify")
  Response<BasicDto> notifyAdmin(@RequestBody NotifyAdminRequest notifyAdminRequest, @RequestAttribute(Misc.INTERCEPTOR_SERVICES_ID) String serviceId);

  @PatchMapping("/challenge-questions/validate")
  Response<TokenDto> validateChallengeQuestionsAnswer(@RequestBody ChallengeQuestionsAnswerRequest challengeQuestionsAnswerRequest);

  @PatchMapping("/password")
  Response<BasicDto> updatePassword(@RequestBody PasswordUpdateRequest passwordResetRequest);

  @GetMapping("/account/lookup")
  Response<AccountLookupDto> accountLookup(@RequestParam(name = "username") String username) throws JsonProcessingException, UnsupportedEncodingException;

  @GetMapping("/extras")
  Response<AuthExtras> getExtras(@RequestParam(name = "fqdn") String fqdn) throws JsonProcessingException;

  @PostMapping("/sso/redirect-url")
  Response<RedirectUrlDto> getSSORedirectUrl(@RequestBody SSORedirectDto ssoRedirectDto) throws UnsupportedEncodingException, JsonProcessingException;

}
