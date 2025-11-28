package com.leucine.services.jaas.sso.azure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.dto.partial.PartialUserDto;
import com.leucine.services.jaas.exception.InvalidTokenException;
import com.leucine.services.jaas.exception.JaasException;
import com.leucine.services.jaas.sso.IOauth2Provider;
import com.leucine.services.jaas.util.DateTimeUtils;
import com.leucine.services.jaas.util.JsonUtils;
import com.leucine.services.jaas.util.TextCompressor;
import com.microsoft.graph.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.leucine.services.jaas.constant.Oauth2.AUTHORIZATION_GRANT_TYPE;
import static com.leucine.services.jaas.constant.Oauth2.Azure.*;
import static com.leucine.services.jaas.constant.Oauth2.Azure.State.LOGIN;

public class AzureProvider implements IOauth2Provider {
  private static final Logger log = LoggerFactory.getLogger(AzureProvider.class);
  private final AzureHandler azureHandler;

  public AzureProvider(AzureHandler azureHandler) {
    this.azureHandler = azureHandler;
  }

  /**
   * Method generates login url for Azure SSO login page with required parameters and scopes to be requested
   *
   * @return login url
   */
  public String getLoginUrl(String username) throws JsonProcessingException {
    AzureOauth2Properties azureOauth2Properties = this.azureHandler.getAzureProperties();
    Map<String, String> state = Map.of(STATE, LOGIN.toString());
    return UriComponentsBuilder.fromHttpUrl(azureOauth2Properties.getIssuer() + azureOauth2Properties.getTenantId() + AUTHORIZATION_ENDPOINT)
      .queryParam(CLIENT_ID, this.azureHandler.getAzureProperties().getClientId())
      .queryParam(RESPONSE_TYPE, CODE)
      .queryParam(SCOPE, OPENID_SCOPE)
      .queryParam(REDIRECT_URI, this.azureHandler.getAzureProperties().getRedirectUri())
      .queryParam(PROMPT, PROMPT_LOGIN)
      .queryParam(RESPONSE_MODE, QUERY)
      .queryParam(LOGIN_HINT, username)
      .queryParam(STATE, JsonUtils.writeValueAsString(TextCompressor.compress(JsonUtils.writeValueAsString(state)))).toUriString();
  }

  /**
   * Method verifies authorization code from Azure SSO login page and returns id token
   * The code returned from the authorization endpoint is a one-time use code.
   * The authorization code is valid for few minutes and can be used only once.
   * The authorization code is bound to the client identifier and redirection URI.
   *
   * @param authorizationCode code returned from Azure SSO login page
   * @param state             state returned from Azure SSO login page
   * @param email             email of the user who is trying to login
   * @see <a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow">Azure Authorization Code Flow</a>
   * @see <a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow#request-an-authorization-code">Request an authorization code</a>
   * @see <a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow#request-an-access-token">Request an access token</a>
   * @see <a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow#request-an-id-token">Request an ID token</a>
   * @see <a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow#validate-the-id-token">Validate the ID token</a>
   */
  public String verifyAuthorizationCode(String authorizationCode, String state, String email, String username) throws IOException {
    log.info("Verifying authorization code from Azure with state: {}, email: {}", state, email);
    AzureOauth2Properties azureOauth2Properties = this.azureHandler.getAzureProperties();
    String tokenUrl = azureOauth2Properties.getIssuer() + azureOauth2Properties.getTenantId() + TOKEN_ENDPOINT;
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add(GRANT_TYPE, AUTHORIZATION_GRANT_TYPE);
    formData.add(CLIENT_ID, this.azureHandler.getAzureProperties().getClientId());
    formData.add(CLIENT_SECRET, this.azureHandler.getAzureProperties().getClientSecret());
    formData.add(CODE, authorizationCode);
    formData.add(REDIRECT_URI, this.azureHandler.getAzureProperties().getRedirectUri());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(tokenUrl, requestEntity, JsonNode.class);
    if (responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new InvalidTokenException(ErrorCode.INVALID_USERNAME_OR_EMAIL);
    } else {
      JsonNode responseBody = responseEntity.getBody();
      String accessToken = responseBody.get(ACCESS_TOKEN).asText();
      validateIfAccessTokenBelongsToLoggedInUser(accessToken, email, username);
      return accessToken;
    }
  }

  /**
   * Method validates if id token belongs to the user who is trying to login
   *
   * @param accessToken id token returned from Azure SSO login page
   * @param email   email of the user who is trying to login
   */
  private void validateIfAccessTokenBelongsToLoggedInUser(String accessToken, String email, String username) throws JsonProcessingException {
    String subject = JsonUtils.getPreferredUsername(accessToken, OID_CLAIM);
    if (!isUserEmailOrUsernameMatchedWithToken(subject, email, username)) {
      throw new JaasException(ErrorCode.USER_DETAILS_CHANGED_ON_SIGN_IN_PAGE);
    }
  }

  public String getRedirectUrl(String username, Map<Object, Object> state, String prompt) throws JsonProcessingException {
    AzureOauth2Properties azureOauth2Properties = this.azureHandler.getAzureProperties();
    String json = JsonUtils.writeValueAsString(state);
    return UriComponentsBuilder.fromHttpUrl(azureOauth2Properties.getIssuer() + azureOauth2Properties.getTenantId() + AUTHORIZATION_ENDPOINT)
      .queryParam(CLIENT_ID, azureHandler.getAzureProperties().getClientId()).queryParam(RESPONSE_TYPE, CODE)
      .queryParam(SCOPE, OPENID_SCOPE)
      .queryParam(REDIRECT_URI, azureHandler.getAzureProperties().getRedirectUri())
      .queryParam(STATE, TextCompressor.compress(json))
      .queryParam(PROMPT, prompt)
      .queryParam(LOGIN_HINT, username)
      .toUriString();
  }

  public String getLogoutUrl(String idToken) {
    return azureHandler.getAzureProperties().getIssuer() + azureHandler.getAzureProperties().getTenantId() + LOGOUT_ENDPOINT;
  }

  public List<PartialUserDto> searchUsers(String query, int limit) {
    return azureHandler.getAzureADGraphConfig().getUsers(query, limit);
  }

  // TODO: extend this logic when we will migrate from client secret to private key

  private String getClientAssertion() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
    PrivateKey privateKey = this.getPrivateKey(this.azureHandler.getAzureProperties().getPrivateKeyPath());
    return Jwts.builder().addClaims(Map.of("aud", this.azureHandler.getAzureProperties().getIssuer() + "/oauth2/v2.0/token")).addClaims(Map.of("exp", DateTimeUtils.nowPlusMinutesToEpochMilli(5) / 1000L)).addClaims(Map.of("iss", this.azureHandler.getAzureProperties().getClientId())).addClaims(Map.of("jti", UUID.randomUUID().toString())).addClaims(Map.of("sub", this.azureHandler.getAzureProperties().getClientId())).addClaims(Map.of("nbf", DateTimeUtils.now())).signWith(privateKey, SignatureAlgorithm.ES384).compact();
  }
  private PrivateKey getPrivateKey(String privateKeyPath) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
    String key = this.readKeyFromFile(privateKeyPath);
    log.info("key: {}", key.toLowerCase());
    byte[] decoded = Base64.getDecoder().decode(key);
    KeyFactory kf = KeyFactory.getInstance("EC");
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
    return kf.generatePrivate(keySpec);
  }

  private String readKeyFromFile(String filePath) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(filePath));
    return lines.stream().filter((line) ->
        !line.startsWith("-----"))
      .collect(Collectors.joining())
      .replaceAll("\\s", "");
  }


  /**
   * Checks if the email or username matches the user principal name of the Azure AD user.
   *
   * <p>This method compares the given email and username (after converting them to lower case)
   * with the user principal name of the Azure AD user associated with the given subject.
   * It performs a case-insensitive comparison. The method is null-safe, meaning it
   * handles null values for email, username, and user principal name gracefully without throwing exceptions.</p>
   *
   * @param subject The subject identifier for the Azure AD user.
   * @param email The email address to be compared. Can be null.
   * @param username The username to be compared. Can be null.
   * @return true if the email or username matches the user principal name of the Azure AD user; false otherwise.
   * @throws NullPointerException if the subject is null.
   */
  private boolean isUserEmailOrUsernameMatchedWithToken(String subject, String email, String username) {
    User user = this.azureHandler.getAzureADGraphConfig().getUser(subject);

    if (user.id == null) {
      return false;
    }

    String userPrincipalNameLower = user.userPrincipalName != null ? user.userPrincipalName.toLowerCase() : null;
    String emailLower = email != null ? email.toLowerCase() : null;
    String usernameLower = username != null ? username.toLowerCase() : null;

    return ObjectUtils.nullSafeEquals(userPrincipalNameLower, emailLower) ||
      ObjectUtils.nullSafeEquals(userPrincipalNameLower, usernameLower);
  }

}
