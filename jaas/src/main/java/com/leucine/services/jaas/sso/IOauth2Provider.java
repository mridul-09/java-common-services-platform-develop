package com.leucine.services.jaas.sso;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leucine.services.jaas.dto.partial.PartialUserDto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;

public interface IOauth2Provider {
  String LOGIN_HINT = "login_hint";

  String getLoginUrl(String username) throws JsonProcessingException;

  String verifyAuthorizationCode(String authorizationCode, String state, String email, String username) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

  String getRedirectUrl(String username, Map<Object, Object> var1, String prompt) throws JsonProcessingException;

  String getLogoutUrl(String idToken);

  List<PartialUserDto> searchUsers(String query, int limit);
}
