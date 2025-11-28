package com.leucine.services.jaas.sso.okta;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.constant.Oauth2;
import com.leucine.services.jaas.dto.partial.PartialUserDto;
import com.leucine.services.jaas.exception.InvalidTokenException;
import com.leucine.services.jaas.exception.JaasException;
import com.leucine.services.jaas.sso.IOauth2Provider;
import com.leucine.services.jaas.sso.OAuthUserDto;
import com.leucine.services.jaas.util.JsonUtils;
import com.leucine.services.jaas.util.TextCompressor;
import com.leucine.services.jaas.util.Utility;
import com.okta.sdk.resource.api.UserApi;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.leucine.services.jaas.constant.Oauth2.Okta.*;

@Slf4j
public class OktaProvider implements IOauth2Provider {
  private final OktaHandler oktaHandler;
  private static ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.registerModule(new JsonNullableModule());
  }

  public OktaProvider(OktaHandler oktaHandler) {
    this.oktaHandler = oktaHandler;
  }

  @Override
  public String getLoginUrl(String username) throws JsonProcessingException {
    String state = JsonUtils.writeValueAsString(Map.of(STATE, State.LOGIN.toString()));
    return UriComponentsBuilder
      .fromHttpUrl(oktaHandler.getOktaProperties().getIssuer() + AUTHORIZATION_ENDPOINT)
      .queryParam(CLIENT_ID, oktaHandler.getOktaProperties().getClientId())
      .queryParam(RESPONSE_TYPE, RESPONSE_TYPE_CODE)
      .queryParam(SCOPE, OPENID_SCOPE)
      .queryParam(REDIRECT_URI, oktaHandler.getOktaProperties().getRedirectUri())
      .queryParam(PROMPT, PROMPT_LOGIN)
      .queryParam(LOGIN_HINT, username)
      .queryParam(STATE, TextCompressor.compress(state))// protect against CSRF
      .toUriString();
  }

  @Override
  public String verifyAuthorizationCode(String authorizationCode, String state, String email, String username) throws JsonProcessingException {
    log.info("Verifying authorization code from Okta with state: {}, email: {}", state, email);
    String tokenUrl = oktaHandler.getOktaProperties().getIssuer() + TOKEN_ENDPOINT;

    // Construct your request to fetch the tokens
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add(GRANT_TYPE, Oauth2.AUTHORIZATION_GRANT_TYPE);
    formData.add(CLIENT_ID, oktaHandler.getOktaProperties().getClientId());
    formData.add(CLIENT_SECRET, oktaHandler.getOktaProperties().getClientSecret());
    formData.add(CODE, authorizationCode);
    formData.add(REDIRECT_URI, oktaHandler.getOktaProperties().getRedirectUri());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<JsonNode> responseEntity = restTemplate
      .postForEntity(tokenUrl, requestEntity, JsonNode.class);

    // Handle the response
    // Handle the error
    if (responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new InvalidTokenException(ErrorCode.INVALID_USERNAME_OR_EMAIL);
    }
    JsonNode responseBody = responseEntity.getBody();
    String idToken = responseBody.get(ID_TOKEN).asText();
    validateIfIdTokenBelongsToLoggedInUser(idToken, email, username);

    return idToken;
  }

  @Override
  public String getRedirectUrl(String username, Map<Object, Object> state, String prompt) throws JsonProcessingException {
    log.info("Getting redirect url from Okta with state: {}", state);
    String json = JsonUtils.writeValueAsString(state);
//    String compressedJson =
    return UriComponentsBuilder
      .fromHttpUrl(oktaHandler.getOktaProperties().getIssuer() + AUTHORIZATION_ENDPOINT)
      .queryParam(CLIENT_ID, oktaHandler.getOktaProperties().getClientId())
      .queryParam(RESPONSE_TYPE, RESPONSE_TYPE_CODE)
      .queryParam(SCOPE, OPENID_SCOPE)
      .queryParam(REDIRECT_URI, (oktaHandler.getOktaProperties().getRedirectUri()))
      .queryParam(STATE, TextCompressor.compress(json))// protect against CSRF
      .queryParam(PROMPT, prompt)
      .queryParam(LOGIN_HINT, username)
      .toUriString();
  }

  @Override
  public String getLogoutUrl(String idToken) {
    return oktaHandler.getOktaProperties().getIssuer() + LOGOUT_ENDPOINT.formatted(idToken, oktaHandler.getOktaProperties().getPostLogoutRedirectUri());
  }

  @Override
  public List<PartialUserDto> searchUsers(String query, int limit) {
    log.info("Getting users from Okta AD with query: {}, limit: {}", query, limit);
    List<PartialUserDto> userSearchList = new ArrayList<>(limit);
    query = query.trim();

    String filterQuery = DEFAULT_USER_FILTER;
    if (!Utility.isEmpty(query)) {
      String[] words = query.split("\\s+");
      if (words.length == 1) {
        filterQuery = Oauth2.Okta.USER_FILTER.formatted(query, query, query, query);
      } else {
        StringBuilder profilePart = new StringBuilder("(profile.firstName sw \"").append(words[0]).append("\" or (");
        for (int i = 1; i < words.length; i++) {
          profilePart.append("profile.lastName sw \"").append(words[i]).append("\"");
          if (i != words.length - 1) {
            profilePart.append("and ");
          }
        }
        filterQuery = profilePart
          .append(") or profile.login sw \"")
          .append(query)
          .append("\" or profile.employeeNumber sw \"")
          .append(query)
          .append("\" or profile.email sw \"")
          .append(query)
          .append("\") and (status eq \"ACTIVE\")")
          .toString();
      }

    }
    OktaOAuth2Properties oktaOAuth2Properties = oktaHandler.getOktaProperties();
    OAuthUserDto mappings = oktaOAuth2Properties.getMappings();

    UserApi userApi = oktaHandler.getUserApi();
    var users = userApi.listUsers(null, null, limit, null, filterQuery, Oauth2.Okta.USERS_SORT_BY_FIRST_NAME, null);

    users.forEach(user -> {
      PartialUserDto partialUserDto = new PartialUserDto();
      JsonNode userNode = mapper.valueToTree(user.getProfile());
      partialUserDto.setUsername(Utility.getValueFromJson(userNode, mappings.getUsername()));
      partialUserDto.setEmail(Utility.getValueFromJson(userNode, mappings.getEmail()));
      partialUserDto.setFirstName(Utility.getValueFromJson(userNode, mappings.getFirstName()));
      partialUserDto.setLastName(Utility.getValueFromJson(userNode, mappings.getLastName()));
      partialUserDto.setEmployeeId(Utility.getValueFromJson(userNode, mappings.getEmployeeId()));
      partialUserDto.setDepartment(Utility.getValueFromJson(userNode, mappings.getDepartment()));
      partialUserDto.setArchived(false);
      partialUserDto.setState(user.getProfile().getState());
      userSearchList.add(partialUserDto);
    });
    return userSearchList;
  }

  private void validateIfIdTokenBelongsToLoggedInUser(String idToken, String email, String username) throws JsonProcessingException {
    String subject = JsonUtils.getPreferredUsername(idToken, SUBJECT_CLAIM);
    if (!isUserEmailOrUsernameMatchedWithToken(subject, email, username)) {
      throw new JaasException(ErrorCode.USER_DETAILS_CHANGED_ON_SIGN_IN_PAGE);
    }
  }

  /**
   * Checks if the email or username matches the email of the Okta user.
   *
   * <p>This method compares the given email and username (after converting them to lower case)
   * with the email of the Okta user associated with the given subject.
   * It performs a case-insensitive comparison and is null-safe, meaning it handles null values for email,
   * username, and the Okta user's email gracefully without throwing exceptions.</p>
   *
   * @param subject The subject identifier for the Okta user. Should not be null.
   * @param email The email address to be compared. Can be null.
   * @param username The username to be compared. Can be null.
   * @return true if the email or username matches the email of the Okta user; false otherwise.
   */
  private boolean isUserEmailOrUsernameMatchedWithToken(String subject, String email, String username) {
    UserApi userApi = oktaHandler.getUserApi();
    var user = userApi.getUser(subject);

    if (Utility.isEmpty(user) || Utility.isEmpty(user.getProfile())) {
      return false;
    }

    String userEmail = user.getProfile().getEmail();
    if (userEmail == null) {
      return false;
    }

    String userEmailLower = userEmail.toLowerCase();
    String emailLower = email != null ? email.toLowerCase() : null;
    String usernameLower = username != null ? username.toLowerCase() : null;

    return ObjectUtils.nullSafeEquals(userEmailLower, emailLower) ||
      ObjectUtils.nullSafeEquals(userEmailLower, usernameLower);
  }

}
