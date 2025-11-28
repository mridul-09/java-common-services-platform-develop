package com.leucine.services.jaas.service.impl;

import com.auth0.jwk.JwkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.leucine.services.jaas.config.AzureADGraphConfig;
import com.leucine.services.jaas.config.JwtConfiguration;
import com.leucine.services.jaas.constant.*;
import com.leucine.services.jaas.dto.*;
import com.leucine.services.jaas.dto.mapper.*;
import com.leucine.services.jaas.dto.partial.PartialFacilityDto;
import com.leucine.services.jaas.dto.partial.PartialOrganisationSettingDto;
import com.leucine.services.jaas.dto.partial.PartialPasswordPolicyDto;
import com.leucine.services.jaas.dto.request.*;
import com.leucine.services.jaas.exception.ForbiddenException;
import com.leucine.services.jaas.exception.InvalidTokenException;
import com.leucine.services.jaas.exception.JaasException;
import com.leucine.services.jaas.exception.ResourceNotFoundException;
import com.leucine.services.jaas.model.*;
import com.leucine.services.jaas.model.helper.BaseEntity;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import com.leucine.services.jaas.model.projection.UserView;
import com.leucine.services.jaas.repository.*;
import com.leucine.services.jaas.service.*;
import com.leucine.services.jaas.sso.IOauth2Provider;
import com.leucine.services.jaas.sso.OAuth2ProviderFactory;
import com.leucine.services.jaas.sso.azure.AzureHandler;
import com.leucine.services.jaas.sso.azure.AzureOauth2Properties;
import com.leucine.services.jaas.sso.okta.OktaHandler;
import com.leucine.services.jaas.sso.okta.OktaOAuth2Properties;
import com.leucine.services.jaas.util.DateTimeUtils;
import com.leucine.services.jaas.util.JsonUtils;
import com.leucine.services.jaas.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {
  private static final String LOGGED_IN = "logged in";
  private static final String LOGGED_OUT = "logged out";
  private final AuthenticationManager authenticationManager;
  private final IUserMapper userMapper;
  private final IFacilityMapper facilityMapper;
  private final IOrganisationMapper organisationMapper;
  private final IOrganisationSettingMapper organisationSettingMapper;
  private final IPasswordHistoryRepository passwordHistoryRepository;
  private final IPasswordPolicyRepository passwordPolicyRepository;
  private final IRolePermissionRepository rolePermissionRepository;
  private final IOrganisationServiceMappingRepository organisationServiceMappingRepository;
  private final ITokenService tokenService;
  private final IUserAuditService userAuditService;
  private final ILicenseService licenseService;
  private final IEmailService emailService;
  private final IUserRepository userRepository;
  private final IUserService userService;
  private final JwtConfiguration jwtConfiguration;
  private final IOrganisationSettingRepository organisationSettingRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final IClientCredentialRepository clientCredentialRepository;
  private final IFacilityRepository facilityRepository;
  private final IPasswordPolicyMapper iPasswordPolicyMapper;


  @Override
  public AuthenticationDto register(UserRegistrationRequest userRegistrationRequest, String serviceId) throws MalformedURLException, JwkException, JsonProcessingException {
    var validationToken = tokenService.extractValidationToken(userRegistrationRequest.getToken()).orElseThrow(() -> new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID));
    if (!validationToken.getPurposes().contains(Misc.ValidationPurpose.REGISTRATION)) {
      throw new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID);
    }
    var userId = Long.valueOf(validationToken.getIdentifier());
    var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Message.Error.RESOURCE_NOT_FOUND));
    userService.isUsernameAvailable(userRegistrationRequest.getUsername());
    List<IErrorCode> errors = userService.validatePasswordAgainstPasswordPolicy(user, userRegistrationRequest.getUsername(), userRegistrationRequest.getConfirmPassword(), userRegistrationRequest.getPassword());
    if (!Utility.isEmpty(errors)) {
      throw new JaasException(errors);
    }
    user.setUsername(userRegistrationRequest.getUsername())
      .setPassword(userRegistrationRequest.getPassword())
      .setPasswordUpdatedAt(DateTimeUtils.now())
      .setState(State.User.REGISTERED);
    user = userRepository.save(user);
    var passwordHistory = new PasswordHistory();
    passwordHistory.setUser(user).setPassword(userRegistrationRequest.getPassword());
    passwordHistoryRepository.save(passwordHistory);
    tokenService.cancelRegistrationToken(user);
    userAuditService.register(userMapper.toPartialUserForAudit(user));
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      log.error("[register] An error occurred while attempting to delay execution during the registration process.");
    }
    // TODO see if the below can be used with authServices' login method
    return authenticate(new AuthenticationRequest().setUsername(userRegistrationRequest.getUsername()).setPassword(userRegistrationRequest.getPassword()), serviceId);

//    OrganisationSetting settings = user.getOrganisation().getSettings();
//    settings.setAccessTokenExpirationInMinutes(jwtConfiguration.getAccessTokenExpirationInMinutes());
//    settings.setRefreshTokenExpirationInMinutes(jwtConfiguration.getRefreshTokenExpirationInMinutes());
//    user.setServiceId(serviceId);
//    List<Facility> facilities = user.getFacilities().stream().sorted(Comparator.comparing(Facility::getName)).collect(Collectors.toList());
//    if (facilities.size() == 1) {
//      Optional<Facility> firstElement = facilities.stream().findFirst();
//      if (firstElement.isPresent()) {
//        user.setCurrentFacilityId(firstElement.get().getId());
//        userAuditService.switchFacility(mapper.toPartialUserForAudit(user), firstElement.get().getName());
//      }
//    }
//    Set<Role> roles = user.getRoles();
//    Set<Long> roleIds = roles.stream().map(BaseEntity::getId).collect(Collectors.toSet());
//    List<RolePermissionMapping> rolePermissionMappings = rolePermissionRepository.findAllByRoleIn(new ArrayList<>(roles));
//    HashMap<String, Boolean> permissionMap = new HashMap<>(rolePermissionMappings.stream().collect(Collectors.toMap(s -> s.getPermission().getName(), s -> true)));
//    Object[] tokens = tokenService.generateAccessAndRefreshToken(user);
//    String accessToken = (String) tokens[0];
//    String refreshToken = (String) tokens[1];
//
//    // If service is c6d8285b72a84efb8fbd608c7cada484 & roles is ACCOUNT_OWNER or SYSTEM_ADMIN send all the facilities in the mapping
//    if (serviceId.equals(Misc.Services.CleenDWI.ID) && (roleIds.contains(Misc.Services.CleenDWI.Roles.ACCOUNT_OWNER.id()) || roleIds.contains(Misc.Services.CleenDWI.Roles.SYSTEM_ADMIN.id()))) {
//      List<Facility> myFacilities = facilities;
//      facilities = new LinkedList<>();
//      facilities.add(Misc.ALL_FACILITY);
//      facilities.addAll(myFacilities);
//      user.setCurrentFacilityId(Misc.ALL_FACILITY_ID);
//    }
//
//    boolean hasSetChallengeQuestion = user.getChallengeQuestion() != null;
//
//    var authenticationDto = new AuthenticationDto();
//    authenticationDto.setId(user.getIdAsString())
//      .setEmployeeId(user.getEmployeeId())
//      .setFirstName(user.getFirstName())
//      .setLastName(user.getLastName())
//      .setTokenType(Misc.BEARER)
//      .setHasSetChallengeQuestion(hasSetChallengeQuestion)
//      .setOrganisation(organisationMapper.toPartialDto(user.getOrganisation()))
//      .setFacilities(facilityMapper.toPartialDto(facilities))
//      .setSettings(organisationSettingMapper.toPartialDto(settings))
//      .setAccessToken(accessToken)
//      .setRefreshToken(refreshToken)
//      .setToken(tokenService.generateValidationToken(new ValidationToken().setIdentifier(user.getIdAsString()).addPurpose(Misc.ValidationPurpose.CHALLENGE_QUESTION_UPDATE)))
//      .setRoles(roles.stream().map(Role::getName).collect(Collectors.toList()))
//      .setPermissions(permissionMap);
//    return authenticationDto;
  }

  @Override
  public AuthenticationDto authenticate(AuthenticationRequest authenticationRequest, String serviceId) throws MalformedURLException, JwkException, JsonProcessingException {
    return login(authenticationRequest.getUsername(), authenticationRequest.getPassword(), authenticationRequest.getCode(), serviceId, false, null, authenticationRequest.getState(), authenticationRequest.getClientId(), authenticationRequest.getClientSecret(), authenticationRequest.getFacilityId());
  }

  @Override
  public AuthenticationDto relogin(ReloginRequest reloginRequest, String serviceId) {
    return login(reloginRequest.getUsername(), reloginRequest.getPassword(), reloginRequest.getCode(), serviceId, true, reloginRequest.getAccessToken(), reloginRequest.getState(), null, null, null);
  }

  private AuthenticationDto login(String username, String password, String code, String serviceId, boolean relogin, String token, String state, String clientId, String clientSecret, String facilityId) {
    User user;
    String idToken = null;
    try {
      if (Utility.isEmpty(clientId)) {
        user = (User) (userService.loadUserByUsername(username));
      } else {
        username = "bot@leucinetech.com";
        user = (User) (userService.loadUserByUsername(username));
      }
    } catch (UsernameNotFoundException | ResourceNotFoundException ex) {
      userAuditService.loginFailedUserNotFound();
      throw new JaasException(ErrorCode.INVALID_CREDENTIALS, ex);
    }
    OrganisationSetting settings = user.getOrganisation().getSettings();
    settings.setAccessTokenExpirationInMinutes(jwtConfiguration.getAccessTokenExpirationInMinutes());
    settings.setRefreshTokenExpirationInMinutes(jwtConfiguration.getRefreshTokenExpirationInMinutes());
    PartialOrganisationSettingDto partialOrganisationSettingDto = mapUserOrganisationSettingsToDto(user);
    try {
      OAuth2ProviderFactory oAuth2ProviderFactory;
      IOauth2Provider authProvider;
      if (user.getUserType() == Misc.UserType.AZURE) {
        AzureOauth2Properties azureOauth2Properties = this.getAzureProperties(user);
        oAuth2ProviderFactory = new OAuth2ProviderFactory(null, new AzureHandler(azureOauth2Properties, new AzureADGraphConfig(azureOauth2Properties)));
        authProvider = oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.AZURE);
        idToken = authProvider.verifyAuthorizationCode(code, state, user.getEmail(), user.getUsername());

      } else if (user.getUserType() == Misc.UserType.OKTA) {
        oAuth2ProviderFactory = new OAuth2ProviderFactory(new OktaHandler(getOktaProperties(user)), null);
        authProvider = oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.OKTA);
        idToken = authProvider.verifyAuthorizationCode(code, state, user.getEmail(), user.getUsername());

      } else if (user.getUserType() == Misc.UserType.SYSTEM) {
        validateClientCredentials(clientId, clientSecret);
        return tokenService.generateClientAccessToken(user, clientId, clientSecret, facilityId);
      } else
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user, password));
    } catch (AuthenticationException ae) {
      userAuditService.loginFailed(userMapper.toPartialUserForAudit(user));
      handleInvalidPasswordAttempt(user, settings.getMaxFailedLoginAttempts());
      throw ae;
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new InvalidTokenException(e);
    }
    user.setServiceId(serviceId);
    Set<Role> roles = user.getRoles();
    Set<Long> roleIds = roles.stream().map(BaseEntity::getId).collect(Collectors.toSet());
    List<RolePermissionMapping> rolePermissionMappings = rolePermissionRepository.findAllByRoleIn(new ArrayList<>(roles));
    HashMap<String, Boolean> permissionMap = new HashMap<>(rolePermissionMappings.stream().collect(Collectors.toMap(s -> s.getPermission().getName(), s -> true)));

    String accessToken = null;
    String refreshToken = null;
    List<PartialFacilityDto> partialFacilityDtos = null;
    List<LicenseDto> licenseDtos = null;
    /*
    TODO Handle the case when session timeout has happened and
      - user's role has changed or
      - user's facility mapping has been updated or
      - user's facility mapping has been updated and the current facility is no longer mapped
     */
    List<Facility> facilities = user.getFacilities();
    if (serviceId.equals(Misc.Services.DWI.ID) && roleIds.stream().anyMatch(Misc.Services.DWI.ALL_FACILITY_ROLES::contains)) {
      List<Facility> myFacilities = facilities;
      facilities = new LinkedList<>();
      Facility globalFacility = facilityRepository.getReferenceById(Misc.ALL_FACILITY_ID);
      facilities.add(globalFacility);
      facilities.addAll(myFacilities);
    }

    if (!relogin) {
      // User is trying to login. In this case we need to fetch facilities and related logics
      if (facilities.size() == 1) {
        Optional<Facility> firstElement = facilities.stream().findFirst();
        if (firstElement.isPresent()) {
          user.setCurrentFacilityId(firstElement.get().getId());
          userAuditService.switchFacility(userMapper.toPartialUserForAudit(user), firstElement.get().getName());
        }
      }
      licenseDtos = licenseService.getDetails(facilities.stream().map(Facility::getId).collect(Collectors.toList()));

      // If service is c6d8285b72a84efb8fbd608c7cada484 & roles is ACCOUNT_OWNER or SYSTEM_ADMIN send all the facilities in the mapping
      if (serviceId.equals(Misc.Services.DWI.ID) && roleIds.stream().anyMatch(Misc.Services.DWI.ALL_FACILITY_ROLES::contains)) {
        user.setCurrentFacilityId(Misc.ALL_FACILITY_ID);
        userAuditService.switchFacility(userMapper.toPartialUserForAudit(user), "All");
      }
      user.setMyMappedFacilities(facilities);
      partialFacilityDtos = facilityMapper.toPartialDto(facilities);
      Object[] tokens = tokenService.generateAccessAndRefreshToken(user);
      accessToken = (String) tokens[0];
      refreshToken = (String) tokens[1];
    } else {
      // User is trying to login after session timeout. In this case, we need to maintain the current state of the user.
      Object[] tokens = tokenService.generateAccessAndRefreshToken(user, token);
      accessToken = (String) tokens[0];
      refreshToken = (String) tokens[1];
      partialFacilityDtos = facilityMapper.toPartialDto(facilities);
    }
    user.setFailedLoginAttempts(0);
    userRepository.save(user);

//    Misc.UserType ssoType = null;
//    if (!Utility.isEmpty(settings.getExtras())) {
//      ssoType = Misc.UserType.valueOf(settings.getExtras().fieldNames().next().toUpperCase());
//    }
//    PartialOrganisationSettingDto partialOrganisationSettingDto = organisationSettingMapper.toPartialDto(settings, ssoType);
//    partialOrganisationSettingDto.setPasswordPolicy(partialPasswordPolicyDto);
    var authenticationDto = new AuthenticationDto();
    authenticationDto.setId(user.getIdAsString())
      .setEmployeeId(user.getEmployeeId())
      .setFirstName(user.getFirstName())
      .setLastName(user.getLastName())
      .setHasSetChallengeQuestion(user.isHasSetChallengeQuestion())
      .setTokenType(Misc.BEARER)
      .setOrganisation(organisationMapper.toPartialDto(user.getOrganisation()))
      .setFacilities(partialFacilityDtos)
      .setLicenses(licenseDtos)
      .setSettings(partialOrganisationSettingDto)
      .setAccessToken(accessToken)
      .setRefreshToken(refreshToken)
      .setToken(!user.isHasSetChallengeQuestion() ? tokenService.generateValidationToken(new ValidationToken().setIdentifier(user.getIdAsString()).addPurpose(Misc.ValidationPurpose.CHALLENGE_QUESTION_UPDATE)) : null)
      .setRoles(roles.stream().map(Role::getName).collect(Collectors.toList()))
      .setPermissions(permissionMap)
      .setMessage(LOGGED_IN)
      .setIdToken(idToken)
      .setFeatures(JsonUtils.convertValue(settings.getFeatureFlags(), new TypeReference<>() {
      }));
    if (user.getUserType() == Misc.UserType.AZURE) {
      authenticationDto.setHasSetChallengeQuestion(true);
    }
    userAuditService.login(userMapper.toPartialUserForAudit(user));
    return authenticationDto;
  }

  private void validateClientCredentials(String clientId, String clientSecret) {
    ClientCredential clientCredential = clientCredentialRepository.getReferenceById(clientId);
    boolean isValidClient = bCryptPasswordEncoder.matches(clientSecret, clientCredential.getClientSecret());
    if (!isValidClient) {
      throw new ForbiddenException(ErrorCode.INVALID_CLIENT_CREDENTIALS);
    }
  }

  private OktaOAuth2Properties getOktaProperties(User user) throws JsonProcessingException {
    OrganisationSetting organisationSetting = organisationSettingRepository.findByOrganisationId(user.getOrganisationId());
    JsonNode extras = organisationSetting.getExtras();
    switch (user.getUserType()) {
      case OKTA -> {
        return JsonUtils.readValue(JsonUtils.writeValueAsString(extras.get("okta")), OktaOAuth2Properties.class);
      }
    }
    return null;
  }

  private AzureOauth2Properties getAzureProperties(User user) throws JsonProcessingException {
    OrganisationSetting organisationSetting = this.organisationSettingRepository.findByOrganisationId(user.getOrganisationId());
    JsonNode extras = organisationSetting.getExtras();
    return JsonUtils.readValue(JsonUtils.writeValueAsString(extras.get("azure")), AzureOauth2Properties.class);
  }

  private void handleInvalidPasswordAttempt(User user, Integer maxFailedLoginAttempts) {
    Integer passwordAttempts = user.getFailedLoginAttempts();
    passwordAttempts = Utility.isNull(passwordAttempts) ? 0 : passwordAttempts;
    if (user.getState() != State.User.REGISTERED_LOCKED) {
      passwordAttempts++;
      user.setFailedLoginAttempts(passwordAttempts);
      if (passwordAttempts > maxFailedLoginAttempts) {
        tokenService.invalidateJtiToken(user.getId());
        user.setState(State.User.REGISTERED_LOCKED).setLockedAt(DateTimeUtils.now());
        userAuditService.lock(userMapper.toPartialUserForAudit(user));
        userRepository.save(user);
        throw new ForbiddenException(ErrorCode.USER_SELF_LOCKED);
      }
      userRepository.save(user);
    }
  }

  @Override
  public AuthenticationDto logout(LogoutRequest logoutRequest) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (tokenService.invalidateJtiToken(principalUser.getId())) {
      userAuditService.logout(principalUser);
    }
    AuthenticationDto authenticationDto = new AuthenticationDto();
    authenticationDto.setId(principalUser.getIdAsString())
      .setEmployeeId(principalUser.getEmployeeId())
      .setFirstName(principalUser.getFirstName())
      .setLastName(principalUser.getLastName())
      .setEmail(principalUser.getEmail())
      .setMessage(LOGGED_OUT);
    User user = userRepository.getReferenceById(principalUser.getId());
    OAuth2ProviderFactory oAuth2ProviderFactory;
    IOauth2Provider oauth2Provider;
    if (user.getUserType() == Misc.UserType.OKTA) {
      try {
        oAuth2ProviderFactory = new OAuth2ProviderFactory(new OktaHandler(getOktaProperties(user)), null);
        oauth2Provider = oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.OKTA);

        authenticationDto.setLogoutUrl(oauth2Provider.getLogoutUrl(logoutRequest.getIdToken()));
      } catch (Exception e) {
        throw new JaasException(e);
      }
    } else if (user.getUserType() == Misc.UserType.AZURE) {
      try {
        oAuth2ProviderFactory = new OAuth2ProviderFactory(null, new AzureHandler(this.getAzureProperties(user), null));
        oauth2Provider = oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.AZURE);
        authenticationDto.setLogoutUrl(oauth2Provider.getLogoutUrl(logoutRequest.getIdToken()));
      } catch (Exception e) {
        throw new JaasException(e);
      }
    }
    return authenticationDto;
  }

  @Override
  public TokenDto validateCredentials(ValidateCredentialsRequest validateCredentialsRequest, String serviceId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = null;
    try {
      user = userRepository.findById(principalUser.getId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    } catch (UsernameNotFoundException | ResourceNotFoundException ex) {
      userAuditService.loginFailedUserNotFound();
      throw ex;
    }
    /*
      TODO: Verify the following validation checks (throw error if)
      - it's state is LOCKED (UNREGISTERED_LOCKED, REGISTERED_LOCKED, ACCOUNT_LOCKED)
      - it's state is INVITE_EXPIRED or INVITE_CANCELLED
      - it's archived is true
      - it's state is NOT REGISTERED (REGISTERED, PASSWORD_EXPIRED)
    */
    validateUser(user);
    try {
      switch (user.getUserType()) {
        case LOCAL ->
          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user, validateCredentialsRequest.getPassword()));
        case OKTA -> {
          OAuth2ProviderFactory oAuth2ProviderFactory = new OAuth2ProviderFactory(new OktaHandler(getOktaProperties(user)), null);
          var oktaProvider = oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.OKTA);
          oktaProvider.verifyAuthorizationCode(validateCredentialsRequest.getCode(), validateCredentialsRequest.getState(), user.getEmail(), user.getUsername());
        }
        case AZURE -> {
          AzureOauth2Properties azureOauth2Properties = this.getAzureProperties(user);
          OAuth2ProviderFactory oAuth2ProviderFactory = new OAuth2ProviderFactory(null, new AzureHandler(azureOauth2Properties, new AzureADGraphConfig(azureOauth2Properties)));
          var azureProvider = oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.AZURE);
          azureProvider.verifyAuthorizationCode(validateCredentialsRequest.getCode(), validateCredentialsRequest.getState(), user.getEmail(), user.getUsername());
        }
      }
    } catch (AuthenticationException | IOException | NoSuchAlgorithmException | InvalidKeySpecException ae) {
      OrganisationSetting settings = user.getOrganisation().getSettings();
      userAuditService.loginFailed(userMapper.toPartialUserForAudit(user));
      handleInvalidPasswordAttempt(user, settings.getMaxFailedLoginAttempts());
      throw ae;
    }
    TokenDto tokenDto = new TokenDto();
    tokenDto.setToken(tokenService.generateValidationToken(new ValidationToken().setIdentifier(user.getIdAsString()).addPurpose(validateCredentialsRequest.getPurpose())));
    return tokenDto;
  }

  private boolean validateUser(User user) {
    if (null != user) {
      if (State.USER_LOCKED_STATES.contains(user.getState())) {
        throw new ForbiddenException(ErrorCode.USER_SELF_LOCKED);
      }
      if (State.User.INVITE_EXPIRED.equals(user.getState())) {
        throw new ForbiddenException(ErrorCode.USER_INVITE_EXPIRED);
      }
      if (user.isArchived()) {
        throw new ForbiddenException(ErrorCode.USER_ARCHIVED);
      }
      return true;
    }
    return false;
  }

  @Override
  public AdditionalVerificationDto additionalVerification(AdditionalVerificationRequest additionalVerificationRequest) {
    boolean isAdditionalVerificationSuccess = false;
    ValidationToken validationToken = tokenService.extractValidationToken(additionalVerificationRequest.getToken()).orElseThrow(() -> new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID));
    if (!validationToken.getPurposes().contains(Misc.ValidationPurpose.ADDITIONAL_VERIFICATION)) {
      throw new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID);
    }
    Long userId = Long.valueOf(validationToken.getIdentifier());
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    if (State.USER_VALID_REGISTERED_STATES.contains(user.getState())) {
      throw new JaasException(ErrorCode.USER_ALREADY_REGISTERED);
    }
    /*
      TODO: Verify the following validation checks (throw error if)
      - it's state is LOCKED (UNREGISTERED_LOCKED, REGISTERED_LOCKED, ACCOUNT_LOCKED)
      - it's state is INVITE_EXPIRED or INVITE_CANCELLED
      - it's archived is true
    */
    validateUser(user);
    String identifier = additionalVerificationRequest.getIdentifier();
    switch (additionalVerificationRequest.getType()) {
      case EMAIL:
        if (user.getEmail().equals(identifier)) {
          isAdditionalVerificationSuccess = true;
        }
        break;
      case EMPLOYEE_ID:
        if (user.getEmployeeId().equals(identifier)) {
          isAdditionalVerificationSuccess = true;
        }
        break;
      default:
        break;
    }
    if (!isAdditionalVerificationSuccess) {
      userAuditService.additionalVerificationFailed(userMapper.toPartialUserForAudit(user));
      handleInvalidAdditionalVerificationAttempt(user, user.getOrganisation().getSettings().getMaxFailedAdditionalVerificationAttempts());
      throw new JaasException(ErrorCode.USER_ADDITIONAL_VERIFICATION_FAILED);
    }
    AdditionalVerificationDto additionalVerificationDto = new AdditionalVerificationDto();
    additionalVerificationDto.setToken(tokenService.generateValidationToken(new ValidationToken().setIdentifier(user.getIdAsString()).addPurpose(Misc.ValidationPurpose.REGISTRATION)))
      .setId(user.getIdAsString())
      .setFirstName(user.getFirstName())
      .setLastName(user.getLastName())
      .setEmployeeId(user.getEmployeeId());
    return additionalVerificationDto;
  }

  private void handleInvalidAdditionalVerificationAttempt(User user, Integer maxAdditionalVerificationAttempt) {
    Integer additionalVerificationAttempt = user.getFailedAdditionalVerificationAttempts();
    additionalVerificationAttempt = Utility.isNull(additionalVerificationAttempt) ? 0 : additionalVerificationAttempt;
    if (user.getState() != State.User.UNREGISTERED_LOCKED) {
      additionalVerificationAttempt++;
      user.setFailedAdditionalVerificationAttempts(additionalVerificationAttempt);
      if (additionalVerificationAttempt > maxAdditionalVerificationAttempt) {
        userAuditService.inviteExpiredAsAdditionalVerificationFailedAttemptsExceeded(userMapper.toPartialUserForAudit(user));
        user.setState(State.User.INVITE_EXPIRED);
        userRepository.save(user);
        throw new ForbiddenException(ErrorCode.USER_INVITE_EXPIRED,
          tokenService.generateValidationToken(new ValidationToken().setIdentifier(user.getIdAsString()).addPurpose(Misc.ValidationPurpose.NOTIFY_ADMIN)));
      }
      userRepository.save(user);
    }
  }

  private void handleInvalidChallengeQuestionAttempt(User user, Integer maxChallengeQuestionAttempt) {
    Integer challengeQuestionAttempt = user.getFailedChallengeQuestionAttempts();
    challengeQuestionAttempt = Utility.isNull(challengeQuestionAttempt) ? 0 : challengeQuestionAttempt;
    if (user.getState() != State.User.ACCOUNT_LOCKED) {
      challengeQuestionAttempt++;
      user.setFailedChallengeQuestionAttempts(challengeQuestionAttempt);
      if (challengeQuestionAttempt > maxChallengeQuestionAttempt) {
        tokenService.invalidateJtiToken(user.getId());
        userAuditService.accountLockedAsChallengeQuestionFailedAttemptsExceeded(userMapper.toPartialUserForAudit(user));
        user.setState(State.User.ACCOUNT_LOCKED);
        user.setLockedAt(DateTimeUtils.now());
        userRepository.save(user);
        throw new ForbiddenException(ErrorCode.USER_ACCOUNT_LOCKED,
          tokenService.generateValidationToken(new ValidationToken().setIdentifier(user.getIdAsString()).addPurpose(Misc.ValidationPurpose.NOTIFY_ADMIN)));
      }
      userRepository.save(user);
    }
  }

  @Override
  public TokenDto validateToken(ValidateTokenRequest validateTokenRequest) {
    Token token = null;
    try {
      token = tokenService.validateAndFetchToken(validateTokenRequest.getToken(), validateTokenRequest.getType());
    } catch (InvalidTokenException exception) {
      if (exception.getErrorCode().code() == ErrorCode.INVALID_TOKEN.code()) {
        userAuditService.invalidSecretKey();
      }
      throw exception;
    }
    TokenDto tokenDto = new TokenDto();
    Misc.ValidationPurpose purpose = null;
    switch (validateTokenRequest.getType()) {
      case REGISTRATION:
        purpose = Misc.ValidationPurpose.ADDITIONAL_VERIFICATION;
        break;
      case PASSWORD_RESET:
        purpose = Misc.ValidationPurpose.PASSWORD_UPDATE;
        break;
      default:
        purpose = Misc.ValidationPurpose.NA;
        break;
    }
    User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    PartialOrganisationSettingDto partialOrganisationSettingDto = mapUserOrganisationSettingsToDto(user);
    switch (user.getState()) {
      case UNREGISTERED_LOCKED:
      case REGISTERED_LOCKED:
      case ACCOUNT_LOCKED:
        throw new ForbiddenException(ErrorCode.USER_SELF_LOCKED);
    }
    if (user.isArchived()) {
      throw new ForbiddenException(ErrorCode.USER_ARCHIVED);
    }
    tokenDto.setToken(tokenService.generateValidationToken(new ValidationToken().setIdentifier(String.valueOf(token.getUserId())).addPurpose(purpose)))
      .setSettings(partialOrganisationSettingDto);
    return tokenDto;
  }

  @Override
  public PrincipalUser authorize(AuthorizationRequest authorizationRequest) {
    return (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Override
  public ValidateIdentityDto validateIdentity(ValidateIdentityRequest validateIdentityRequest) {
    User user = null;
    try {
      user = (User) userService.loadUserByUsername(validateIdentityRequest.getIdentity().toLowerCase());
    } catch (UsernameNotFoundException | ResourceNotFoundException ex) {
      throw new JaasException(ErrorCode.INVALID_USERNAME_OR_EMAIL, ex);
    }
    switch (user.getState()) {
      case UNREGISTERED_LOCKED:
      case REGISTERED_LOCKED:
      case ACCOUNT_LOCKED:
        throw new ForbiddenException(ErrorCode.USER_SELF_LOCKED);
    }
    if (user.isArchived()) {
      throw new ForbiddenException(ErrorCode.USER_ARCHIVED);
    }
    String email = null;
    if (null != user.getEmail()) {
      email = maskEmail(user.getEmail());
    }
    boolean hasSetChallengeQuestion = user.getChallengeQuestion() != null;
    return new ValidateIdentityDto()
      .setEmail(email)
      .setHasSetChallengeQuestion(hasSetChallengeQuestion);
  }

  private String maskEmail(String email) {
    StringBuilder builder = new StringBuilder(email);
    int lastIndexDot = builder.lastIndexOf(".");
    var i = 1;
    do {
      if (builder.charAt(i) == '@') {
        i++;
      } else {
        builder.setCharAt(i, '*');
      }
      i++;
    } while (i < lastIndexDot);
    return builder.toString();
  }

  @Override
  public BasicDto resetToken(GeneratePasswordResetTokenRequest generatePasswordResetTokenRequest) {
    User user = null;
    try {
      user = (User) userService.loadUserByUsername(generatePasswordResetTokenRequest.getIdentity().toLowerCase());
    } catch (UsernameNotFoundException | ResourceNotFoundException ex) {
      throw new JaasException(ErrorCode.INVALID_USERNAME_OR_EMAIL, ex);
    }

    switch (user.getState()) {
      case UNREGISTERED_LOCKED:
      case REGISTERED_LOCKED:
      case ACCOUNT_LOCKED:
        throw new ForbiddenException(ErrorCode.USER_SELF_LOCKED);
    }
    if (user.isArchived()) {
      throw new ForbiddenException(ErrorCode.USER_ARCHIVED);
    }
    if (!State.USER_VALID_REGISTERED_STATES.contains(user.getState())) {
      throw new JaasException(ErrorCode.USER_NOT_REGISTERED);
    }
    String passwordResetToken = tokenService.generatePasswordResetToken(user);
    user.setToken(passwordResetToken);
    userService.sendEmail(user, passwordResetToken, Action.Email.TOKEN_RESET, Misc.Services.DWI.ID);
    userAuditService.resetToken(userMapper.toPartialUserForAudit(user), null);
    return BasicDto.builder().message(Message.SUCCESS).build();
  }

  @Override
  public BasicDto notifyAdmin(NotifyAdminRequest notifyAdminRequest, String serviceId) {
    User user;
    try {
      user = (User) userService.loadUserByUsername(notifyAdminRequest.getIdentity().toLowerCase());
    } catch (UsernameNotFoundException | ResourceNotFoundException ex) {
      throw new JaasException(ErrorCode.INVALID_USERNAME_OR_EMAIL, ex);
    }

    if (user.isArchived()) {
      throw new ForbiddenException(ErrorCode.USER_ARCHIVED);
    }
    OrganisationServiceMapping organisationServiceMapping = organisationServiceMappingRepository.findByOrganisationIdAndServiceId(user.getOrganisationId(), serviceId);
    Services service = organisationServiceMapping.getService();
    String fqdn = organisationServiceMapping.getFqdn();
    if (!"/".equals(fqdn.substring(fqdn.length() - 1))) {
      fqdn = fqdn + "/";
    }
    String fullName = user.getFirstName() + (user.getLastName() != null ? " " + user.getLastName() : "");
    Map<String, String> attributes = new HashMap<>();
    attributes.put(Email.USER_FULL_NAME, fullName);
    attributes.put(Email.EMPLOYEE_ID, user.getEmployeeId());
    attributes.put(Email.LOGIN_LINK, fqdn + service.getLoginPath());
    attributes.put(Email.REGISTRATION_LINK, fqdn + service.getRegistrationPath());
    attributes.put(Email.RESET_PASSWORD_LINK, fqdn + service.getResetPasswordPath());
    Set<String> emails = userRepository.getAllByOrganisationIdAndRoleId(user.getOrganisationId(), Misc.Services.DWI.Roles.SYSTEM_ADMIN.id()).stream().map(UserView::getEmail).collect(Collectors.toSet());
    EmailRequest emailRequest = null;
    switch (notifyAdminRequest.getPurpose()) {
      case INVITE_EXPIRED:
        userAuditService.administratorContactedForResetInvite(userMapper.toPartialUserForAudit(user));
        emailRequest = EmailRequest.builder().to(emails).templateName(Email.EMAIL_NOTIFY_ADMIN_INVITE_EXPIRED_TEMPLATE).subject(Email.SUBJECT_NOTIFY_ADMIN_INVITE_EXPIRED).attributes(attributes).build();
        break;
      case PASSWORD_RECOVERY_KEY_EXPIRED:
        userAuditService.administratorContactedForPasswordRecovery(userMapper.toPartialUserForAudit(user));
        emailRequest = EmailRequest.builder().to(emails).templateName(Email.EMAIL_NOTIFY_ADMIN_PASSWORD_RECOVERY_KEY_EXPIRED_TEMPLATE).subject(Email.SUBJECT_NOTIFY_ADMIN_PASSWORD_RECOVERY_KEY_EXPIRED).attributes(attributes).build();
        break;
      case PASSWORD_RECOVERY_ACCOUNT_LOCKED:
        userAuditService.administratorContactedForUnlockAccount(userMapper.toPartialUserForAudit(user));
        emailRequest = EmailRequest.builder().to(emails).templateName(Email.EMAIL_NOTIFY_ADMIN_PASSWORD_RECOVERY_ACCOUNT_LOCKED_TEMPLATE).subject(Email.SUBJECT_NOTIFY_ADMIN_PASSWORD_RECOVERY_ACCOUNT_LOCKED).attributes(attributes).build();
        break;
      case PASSWORD_RECOVERY_CHALLENGE_QUESTION_NOT_SET:
        userAuditService.administratorContactedForPasswordRecovery(userMapper.toPartialUserForAudit(user));
        emailRequest = EmailRequest.builder().to(emails).templateName(Email.EMAIL_NOTIFY_ADMIN_PASSWORD_RECOVERY_CHALLENGE_QUESTION_NOT_SET_TEMPLATE).subject(Email.SUBJECT_NOTIFY_ADMIN_PASSWORD_RECOVERY_CHALLENGE_QUESTION_NOT_SET).attributes(attributes).build();
        break;
    }
    if (null != emailRequest) {
      emailService.sendEmail(emailRequest);
    }
    return BasicDto.builder().message(Message.SUCCESS).build();
  }

  @Override
  public TokenDto validateChallengeQuestionsAnswer(ChallengeQuestionsAnswerRequest challengeQuestionsAnswerRequest) {
    User user;
    PartialOrganisationSettingDto partialOrganisationSettingDto;
    try {
      user = (User) userService.loadUserByUsername(challengeQuestionsAnswerRequest.getIdentity().toLowerCase());
      partialOrganisationSettingDto = mapUserOrganisationSettingsToDto(user);
    } catch (UsernameNotFoundException | ResourceNotFoundException ex) {
      throw new JaasException(ErrorCode.INVALID_USERNAME_OR_EMAIL, ex);
    }
    switch (user.getState()) {
      case REGISTERED:
        break;
      case UNREGISTERED:
        throw new ForbiddenException(ErrorCode.USER_NOT_REGISTERED);
      case INVITE_EXPIRED:
        throw new ForbiddenException(ErrorCode.USER_INVITE_EXPIRED);
      case INVITE_CANCELLED:
        throw new ForbiddenException(ErrorCode.USER_INVITE_CANCELLED);
      case UNREGISTERED_LOCKED:
      case REGISTERED_LOCKED:
      case ACCOUNT_LOCKED:
        throw new ForbiddenException(ErrorCode.USER_SELF_LOCKED);
    }
    if (user.isArchived()) {
      throw new ForbiddenException(ErrorCode.USER_ARCHIVED);
    }
    if (!user.getChallengeQuestion().getId().equals(challengeQuestionsAnswerRequest.getId()) || !user.getChallengeQuestionsAnswer().equals(challengeQuestionsAnswerRequest.getAnswer())) {
      userAuditService.challengeQuestionsAnswerValidationFailed(userMapper.toPartialUserForAudit(user));
      handleInvalidChallengeQuestionAttempt(user, user.getOrganisation().getSettings().getMaxFailedChallengeQuestionAttempts());
      throw new JaasException(ErrorCode.USER_CHALLENGE_QUESTION_VALIDATION_FAILED);
    }
    return new TokenDto().setToken(tokenService.generateValidationToken(new ValidationToken().setIdentifier(user.getIdAsString()).addPurpose(Misc.ValidationPurpose.PASSWORD_UPDATE)))
      .setSettings(partialOrganisationSettingDto);
  }

  @Override
  public AuthExtras getExtras(String fqdn) {
    Long organisationId = organisationServiceMappingRepository.findByFqdn(fqdn);
    JsonNode extras = JsonUtils.createObjectNode();
    if (null != organisationId) {
      extras = organisationSettingRepository.findByOrganisationId(organisationId).getExtras();
      return AuthExtras.builder()
        .extras(JsonUtils.removeUnwantedFieldIfExists(extras))
        .build();
    } else {
      return AuthExtras.builder()
        .extras(extras)
        .message("No organisation found for the given fqdn: " + fqdn)
        .build();
    }

  }

  @Override
  public AccountLookupDto accountLookup(String username) throws JsonProcessingException {
    User user = (User) userService.loadUserByUsername(username);
    JsonNode extras = organisationSettingRepository.findByOrganisationId(user.getOrganisationId()).getExtras();
    OAuth2ProviderFactory oAuth2ProviderFactory;

    switch (user.getUserType()) {
      case LOCAL -> {
        return AccountLookupDto.builder()
          .username(user.getUsername())
          .type(user.getUserType().toString())
          .extra(JsonUtils.removeUnwantedFieldIfExists(extras))
          .build();
      }
      case OKTA -> {
        oAuth2ProviderFactory = new OAuth2ProviderFactory(new OktaHandler(getOktaProperties(user)), null);

        return AccountLookupDto.builder()
          .username(user.getUsername())
          .type(user.getUserType().toString())
          .extra(JsonUtils.removeUnwantedFieldIfExists(extras))
          .redirectUrl(oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.OKTA).getLoginUrl(user.getUsername()))
          .build();
      }
      case AZURE -> {
        oAuth2ProviderFactory = new OAuth2ProviderFactory(null, new AzureHandler(this.getAzureProperties(user), null));
        return AccountLookupDto.builder()
          .username(user.getUsername())
          .type(user.getUserType().toString())
          .extra(JsonUtils.removeUnwantedFieldIfExists(extras))
          .redirectUrl(oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.AZURE).getLoginUrl(user.getUsername()))
          .build();

      }
      default -> throw new JaasException(ErrorCode.INVALID_USER_TYPE);
    }
  }

  @Override
  public RedirectUrlDto getSSORedirectUrl(SSORedirectDto ssoRedirectDto) throws JsonProcessingException {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userRepository.findById(principalUser.getId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    JsonNode extras = user.getOrganisation().getSettings().getExtras();
    switch (user.getUserType()) {
      case OKTA -> {
        OktaOAuth2Properties oktaOAuth2Properties = JsonUtils.readValue(JsonUtils.writeValueAsString(extras.get("okta")), OktaOAuth2Properties.class);
        OAuth2ProviderFactory oAuth2ProviderFactory = new OAuth2ProviderFactory(new OktaHandler(getOktaProperties(user)), null);
        IOauth2Provider oAuth2ProviderI = oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.OKTA);

        return RedirectUrlDto.builder()
          .redirectUrl(oAuth2ProviderI.getRedirectUrl(user.getUsername(), ssoRedirectDto.getData(), (Utility.isEmpty(oktaOAuth2Properties.getPrompt())? Oauth2.Okta.PROMPT_LOGIN : oktaOAuth2Properties.getPrompt())))
          .build();
      }
      case AZURE -> {
        AzureOauth2Properties azureOauth2Properties = JsonUtils.readValue(JsonUtils.writeValueAsString(extras.get("azure")), AzureOauth2Properties.class);
        OAuth2ProviderFactory oAuth2ProviderFactory = new OAuth2ProviderFactory(null, new AzureHandler(this.getAzureProperties(user), null));
        IOauth2Provider oAuth2Provider = oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.AZURE);

        return RedirectUrlDto.builder()
          .redirectUrl(oAuth2Provider.getRedirectUrl(user.getUsername(), ssoRedirectDto.getData(), (Utility.isEmpty(azureOauth2Properties.getPrompt())? Oauth2.Okta.PROMPT_LOGIN : azureOauth2Properties.getPrompt())))
          .build();
      }

      default -> throw new JaasException(ErrorCode.INVALID_USER_TYPE);
    }
  }

  @Override
  public BasicDto updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
    ValidationToken validationToken = tokenService.extractValidationToken(passwordUpdateRequest.getToken()).orElseThrow(() -> new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID));
    if (!validationToken.getPurposes().contains(Misc.ValidationPurpose.PASSWORD_UPDATE)) {
      throw new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID);
    }
    User user = userRepository.findById(Long.valueOf(validationToken.getIdentifier())).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    List<IErrorCode> errors = userService.validatePasswordAgainstPasswordPolicy(user, user.getUsername(), passwordUpdateRequest.getConfirmPassword(), passwordUpdateRequest.getPassword());
    if (!Utility.isEmpty(errors)) {
      throw new JaasException(errors);
    }
    if (user.getState().equals(State.User.PASSWORD_EXPIRED)) {
      user.setState(State.User.REGISTERED);
    }
    user.setPassword(passwordUpdateRequest.getPassword()).setPasswordUpdatedAt(DateTimeUtils.now());
    user = userRepository.save(user);
    PasswordHistory passwordHistory = new PasswordHistory();
    passwordHistory.setUser(user).setPassword(passwordUpdateRequest.getPassword());
    passwordHistoryRepository.save(passwordHistory);
    userAuditService.updatePassword(userMapper.toPartialUserForAudit(user));
    return BasicDto.builder().id(String.valueOf(user.getId())).message(Message.PASSWORD_UPDATED).build();
  }

  @Override
  public TokenDto refreshToken(RefreshTokenRequest refreshTokenRequest) {
    // TODO optimize the logic
    PrincipalUser principalUser = tokenService.forcefullyGetPrincipalUser(refreshTokenRequest.getAccessToken());
    String username = tokenService.getUsername(refreshTokenRequest.getRefreshToken(), Type.Token.REFRESH);
    User user = (User) userService.loadUserByUsername(username);
    TokenDto tokenDto = new TokenDto();
    tokenDto.setAccessToken(tokenService.generateAccessToken(principalUser, user));
    return tokenDto;
  }

  private PartialOrganisationSettingDto mapUserOrganisationSettingsToDto(User user) {
    PartialOrganisationSettingDto partialOrganisationSettingDto;
    PartialPasswordPolicyDto partialPasswordPolicyDto;
    OrganisationSetting settings = user.getOrganisation().getSettings();
    Misc.UserType ssoType = null;
    if (!Utility.isEmpty(settings.getExtras())) {
      ssoType = Misc.UserType.valueOf(settings.getExtras().fieldNames().next().toUpperCase());
    }
    partialOrganisationSettingDto = organisationSettingMapper.toPartialDto(settings, ssoType);
    partialPasswordPolicyDto = iPasswordPolicyMapper.toPartialDto(user.getOrganisation().getPasswordPolicy());
    partialOrganisationSettingDto.setPasswordPolicy(partialPasswordPolicyDto);
    return partialOrganisationSettingDto;
  }
}
