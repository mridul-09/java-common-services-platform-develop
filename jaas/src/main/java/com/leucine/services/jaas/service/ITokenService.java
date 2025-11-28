package com.leucine.services.jaas.service;

import com.leucine.services.jaas.constant.Type;
import com.leucine.services.jaas.dto.AuthenticationDto;
import com.leucine.services.jaas.dto.ValidationToken;
import com.leucine.services.jaas.model.Token;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface ITokenService {
  Object[] generateAccessAndRefreshToken(User user);

  Object[] generateAccessAndRefreshToken(User user, String token);

  String generateAccessToken(User user);

  String generateAccessToken(PrincipalUser principalUser, User user);

  String generateRegistrationToken(User user);

  String generatePasswordResetToken(User user);

  PrincipalUser getPrincipalUser(String token);

  PrincipalUser forcefullyGetPrincipalUser(String token);

  PrincipalUser getPrincipalUser(String token, Claims claims);

  boolean invalidateJtiToken(Long userId);

  String generateValidationToken(ValidationToken validationToken);

  Optional<ValidationToken> extractValidationToken(String token);

  String getUsername(String token, Type.Token type);

  Token validateAndFetchToken(String token, Type.Token type);

  boolean cleanup(Token token);

  boolean isAuthorized(String token, Type.Token type, HttpServletRequest request);

  void cancelRegistrationToken(User user);

  void cancelPasswordResetToken(User user);

  AuthenticationDto generateClientAccessToken(User user, String clientId, String clientSecret, String facilityId);
}
