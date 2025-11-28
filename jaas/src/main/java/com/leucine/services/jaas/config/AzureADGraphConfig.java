package com.leucine.services.jaas.config;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.leucine.services.jaas.dto.partial.PartialUserDto;
import com.leucine.services.jaas.sso.OAuthUserDto;
import com.leucine.services.jaas.sso.azure.AzureOauth2Properties;
import com.leucine.services.jaas.util.JsonUtils;
import com.leucine.services.jaas.util.Utility;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;
import com.microsoft.graph.requests.UserCollectionRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class AzureADGraphConfig {
  private GraphServiceClient<Request> appClient;
  private final AzureOauth2Properties azureOauth2Properties;

  public AzureADGraphConfig(AzureOauth2Properties azureOauth2Properties) {
    this.azureOauth2Properties = azureOauth2Properties;
    ensureGraphForAppOnlyAuth();
  }

  private void ensureGraphForAppOnlyAuth() {
    log.info("[ensureGraphForAppOnlyAuth] Attempting to authenticate");
    String clientId = azureOauth2Properties.getClientId();
    String tenantId = azureOauth2Properties.getTenantId();
    String clientSecret = azureOauth2Properties.getClientSecret();
    final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
      .clientId(clientId)
      .tenantId(tenantId)
      .clientSecret(clientSecret)
      .build();


    if (appClient == null) {
      final TokenCredentialAuthProvider authProvider =
        new TokenCredentialAuthProvider(
          List.of("https://graph.microsoft.com/.default"), clientSecretCredential);

      appClient = GraphServiceClient.builder()
        .authenticationProvider(authProvider)
        .buildClient();
    }
  }

  public List<PartialUserDto> getUsers(String filterBy, int limit) {

    UserCollectionRequest users = getUserCollectionRequest(filterBy, limit, azureOauth2Properties);

    UserCollectionPage userCollectionPage;
    if (!Utility.isEmpty(filterBy)) {
      userCollectionPage = users
        .filter(String.format("startsWith(displayName, '%s') or startsWith(mail, '%s') or startsWith(userPrincipalName, '%s') or startsWith(surName, '%s')", filterBy, filterBy, filterBy, filterBy))
        .orderBy("displayName")
        .count(true)
        .get();
    } else {
      userCollectionPage = users.get();
    }

    OAuthUserDto mappings = azureOauth2Properties.getMappings();

    List<PartialUserDto> userSearchList = new ArrayList<>();
    for (User user : userCollectionPage.getCurrentPage()) {
      PartialUserDto partialUserDto = new PartialUserDto();
      JsonNode userNode = JsonUtils.valueToNode(user);

      String nameKey = mappings.getName();
      String employeeIdKey = mappings.getEmployeeId();
      String emailKey = mappings.getEmail();
      String departmentKey = mappings.getDepartment();
      String usernameKey = mappings.getUsername();
      String lastNameKey = mappings.getLastName();

      String displayName = userNode.get(nameKey).asText("");
      if (!Utility.isEmpty(displayName)) {
        String[] splitDisplayName = displayName.split(" ");
        partialUserDto.setFirstName(splitDisplayName[0]);
        if (Utility.isEmpty(lastNameKey)) {
          partialUserDto.setLastName(splitDisplayName.length > 1 ? splitDisplayName[1] : "");
        } else {
          partialUserDto.setLastName(userNode.get(lastNameKey).asText(""));
        }
      }
      partialUserDto.setUsername(userNode.get(usernameKey).asText(""));
      partialUserDto.setEmail(userNode.get(emailKey).asText(""));
      partialUserDto.setEmployeeId(userNode.get(employeeIdKey).asText(""));
      partialUserDto.setDepartment(userNode.get(departmentKey).asText(""));
      userSearchList.add(partialUserDto);
    }
    return userSearchList;
  }

  @NotNull
  private UserCollectionRequest getUserCollectionRequest(String filterBy, int limit, AzureOauth2Properties azureOauth2Properties) {
    log.info("[getUsers] Attempting to get users {}", filterBy);
    final LinkedList<Option> requestOptions = prepareRequestOptions();
    StringBuilder sb = prepareUserDetailsMapping(azureOauth2Properties);
    return appClient.users()
      .buildRequest(requestOptions)
      .select(sb.toString())
      .top(limit);
  }

  @NotNull
  private static LinkedList<Option> prepareRequestOptions() {
    final LinkedList<Option> requestOptions = new LinkedList<>();
    requestOptions.add(new HeaderOption("ConsistencyLevel", "eventual"));
    return requestOptions;
  }

  @NotNull
  private static StringBuilder prepareUserDetailsMapping(AzureOauth2Properties azureOauth2Properties) {
    OAuthUserDto mappings = azureOauth2Properties.getMappings();
    StringBuilder sb = new StringBuilder();

    String name = mappings.getName();
    String employeeId = mappings.getEmployeeId();
    String email = mappings.getEmail();
    String department = mappings.getDepartment();
    String username = mappings.getUsername();
    String firstName = mappings.getFirstName();
    String lastName = mappings.getLastName();

    sb.append(firstName).append(",").append(lastName).append(",").append(name).append(",").append(employeeId).append(",").append(email).append(",").append(username).append(",").append(department);
    return sb;
  }

  public User getUser(String userId) {
    final LinkedList<Option> requestOptions = prepareRequestOptions();
    return appClient.users(userId)
      .buildRequest(requestOptions)
      .get();
  }

}
