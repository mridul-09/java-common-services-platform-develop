package com.leucine.services.jaas.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.leucine.services.jaas.config.AzureADGraphConfig;
import com.leucine.services.jaas.constant.*;
import com.leucine.services.jaas.dto.*;
import com.leucine.services.jaas.dto.mapper.IChallengeQuestionMapper;
import com.leucine.services.jaas.dto.mapper.IFacilityMapper;
import com.leucine.services.jaas.dto.mapper.IUserMapper;
import com.leucine.services.jaas.dto.partial.PartialFetchUserDto;
import com.leucine.services.jaas.dto.partial.PartialUserDto;
import com.leucine.services.jaas.dto.request.*;
import com.leucine.services.jaas.exception.*;
import com.leucine.services.jaas.model.*;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import com.leucine.services.jaas.model.helper.SpecificationBuilder;
import com.leucine.services.jaas.model.helper.search.SearchCriteria;
import com.leucine.services.jaas.model.helper.search.SearchOperator;
import com.leucine.services.jaas.repository.*;
import com.leucine.services.jaas.service.IEmailService;
import com.leucine.services.jaas.service.ITokenService;
import com.leucine.services.jaas.service.IUserAuditService;
import com.leucine.services.jaas.service.IUserService;
import com.leucine.services.jaas.sso.IOauth2Provider;
import com.leucine.services.jaas.sso.OAuth2ProviderFactory;
import com.leucine.services.jaas.sso.azure.AzureHandler;
import com.leucine.services.jaas.sso.azure.AzureOauth2Properties;
import com.leucine.services.jaas.sso.okta.OktaHandler;
import com.leucine.services.jaas.sso.okta.OktaOAuth2Properties;
import com.leucine.services.jaas.util.DateTimeUtils;
import com.leucine.services.jaas.util.JsonUtils;
import com.leucine.services.jaas.util.UUIDGenerator;
import com.leucine.services.jaas.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
  private static final char[] LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
  private static final char[] UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  private static final char[] NUMERIC_CHARACTERS = "0123456789".toCharArray();
  private static final char[] SPECIAL_CHARACTERS = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".toCharArray();

  static {
    Arrays.sort(LOWERCASE_CHARACTERS);
    Arrays.sort(UPPERCASE_CHARACTERS);
    Arrays.sort(NUMERIC_CHARACTERS);
    Arrays.sort(SPECIAL_CHARACTERS);
  }

  private final IEmailService emailService;
  private final IFacilityRepository facilityRepository;
  private final IOrganisationRepository organisationRepository;
  private final IChallengeQuestionRepository challengeQuestionRepository;
  private final IRoleRepository roleRepository;
  private final ITokenService tokenService;
  private final IUserAuditService userAuditService;
  private final IPasswordHistoryRepository passwordHistoryRepository;
  private final IUserMapper mapper;
  private final IFacilityMapper facilityMapper;
  private final IOrganisationServiceMappingRepository organisationServiceMappingRepository;
  private final IChallengeQuestionMapper challengeQuestionMapper;
  private final IUserRepository repository;
  private final IOrganisationSettingRepository organisationSettingRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    log.info("[loadUserByUsername] Request to find user by username [{}]", username);
    final String cred = username.toLowerCase();
    return repository.findByUsernameOrEmail(cred, cred).orElseThrow(() -> new UsernameNotFoundException(cred + " not found"));
  }

  @Override
  @Transactional
  public PrincipalUser create(UserAddRequest userAddRequest) {
    log.info("[create] Request to create/add a user, userAddRequest [{}]", userAddRequest);
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Set<Long> roleIds = userAddRequest.getRoles().stream().map(r -> Long.valueOf(r.getId())).collect(Collectors.toSet());
    Set<Long> facilityIds = userAddRequest.getFacilities().stream().map(f -> Long.valueOf(f.getId())).collect(Collectors.toSet());
    List<Facility> facilities = facilityRepository.findAllById(facilityIds);
    if (!(principalUser.getServiceId().equals(Misc.Services.DWI.ID) && roleIds.stream().anyMatch(Misc.Services.DWI.ALL_FACILITY_ROLES::contains))) {
      if (Utility.isEmpty(facilities)) {
        throw new JaasException(Message.Error.ATLEAST_ONE_FACILITY_REQUIRED);
      }
    } else {
      facilities = facilityRepository.findAllByOrganisationId(principalUser.getOrganisationId());
    }

    validateDuplicateEmployeeId(userAddRequest.getEmployeeId(), principalUser.getOrganisationId());
    if (!Utility.isEmpty(userAddRequest.getEmail())) {
      validateDuplicateUsernameOrEmail(userAddRequest.getEmail());
    }
    if (!Utility.isEmpty(userAddRequest.getUsername())) {
      validateDuplicateUsernameOrEmail(userAddRequest.getUsername());
    }

    User user = new User();
    user.setEmployeeId(userAddRequest.getEmployeeId())
      .setFirstName(userAddRequest.getFirstName())
      .setLastName(userAddRequest.getLastName())
      .setEmail(userAddRequest.getEmail())
      .setDepartment(userAddRequest.getDepartment())
      .setUserType(userAddRequest.getUserType())
      .setUsername(userAddRequest.getUsername() == null ? UUIDGenerator.getInstance().generateUnique() : userAddRequest.getUsername())
      .setState(userAddRequest.getUserType() == Misc.UserType.LOCAL ? State.User.UNREGISTERED : State.User.REGISTERED)
      .setCreatedBy(repository.getOne(principalUser.getId()))
      .setModifiedBy(repository.getOne(principalUser.getId()));

    Organisation organisation = organisationRepository.findById(principalUser.getOrganisationId())
      .orElseThrow(() -> new ResourceNotFoundException(Message.Error.ORGANISATION_NOT_FOUND));
    user.setOrganisation(organisation);
    user.setOrganisationId(organisation.getId());
    user = repository.save(user);
    List<Role> roles = roleRepository.findAllById(roleIds);
    user.setRoles(roles);
    user.setFacilities(facilities);
    user = repository.save(user);
    if (userAddRequest.getUserType() == Misc.UserType.LOCAL) {
      String registrationToken = tokenService.generateRegistrationToken(user);
      user.setToken(registrationToken);
      sendEmail(user, registrationToken, Action.Email.INVITE, principalUser.getServiceId());
    }

    userAuditService.addUser(mapper.toPartialUserForAudit(user), userAddRequest.getReason(),principalUser,roles);
    return mapper.toPrincipalUser(user);
  }

  private boolean validateDuplicateEmployeeId(String employeeId, Long organisationId) {
    if (repository.existsByEmployeeIdAndOrganisationId(employeeId, organisationId)) {
      throw new DuplicateException(ErrorCode.USER_WITH_EMPLOYEE_ID_ALREADY_EXISTS);
    }
    return true;
  }

  private boolean validateDuplicateEmail(String email) {
    if (repository.existsByEmail(email)) {
      throw new DuplicateException(ErrorCode.USER_WITH_EMAIL_ALREADY_EXISTS);
    }
    return true;
  }

  @Override
  public boolean sendEmail(User user, String token, Action.Email action, String serviceId) {
    if (!Utility.isNull(user.getEmail())) {
      String email = user.getEmail();
      OrganisationServiceMapping organisationServiceMapping = organisationServiceMappingRepository.findByOrganisationIdAndServiceId(user.getOrganisationId(), serviceId);
      Services service = organisationServiceMapping.getService();
      String fqdn = organisationServiceMapping.getFqdn();
      if (!"/".equals(fqdn.substring(fqdn.length() - 1))) {
        fqdn = fqdn + "/";
      }
      Map<String, String> attributes = new HashMap<>();
      String fullName = user.getFirstName() + (user.getLastName() != null ? " " + user.getLastName() : "");
      attributes.put(Email.TOKEN, token);
      attributes.put(Email.USER_FULL_NAME, fullName);
      attributes.put(Email.LOGIN_LINK, fqdn + service.getLoginPath());
      attributes.put(Email.REGISTRATION_LINK, fqdn + service.getRegistrationPath());
      attributes.put(Email.RESET_PASSWORD_LINK, fqdn + service.getResetPasswordPath());
      EmailRequest emailRequest = null;
      switch (action) {
        case RESEND_INVITE:
          emailRequest = EmailRequest.builder().to(Collections.singleton(email))
            .templateName(Email.EMAIL_REGISTRATION_REMINDER_TEMPLATE).subject(Email.SUBJECT_REMINDER_TO_REGISTER).attributes(attributes).build();
          emailService.sendEmail(emailRequest);
          break;
        case INVITE:
          emailRequest = EmailRequest.builder().to(Collections.singleton(email))
            .templateName(Email.EMAIL_REGISTRATION_TEMPLATE).subject(Email.SUBJECT_REGISTRATION_INVITE).attributes(attributes).build();
          emailService.sendEmail(emailRequest);
          break;
        case TOKEN_RESET:
          emailRequest = EmailRequest.builder().to(Collections.singleton(email))
            .templateName(Email.EMAIL_RESET_PASSWORD_TEMPLATE).subject(Email.SUBJECT_RESET_PASSWORD).attributes(attributes).build();
          emailService.sendEmail(emailRequest);
          break;
      }
      return true;
    }
    return false;
  }

  @Override
  public UserDto getById(Long userId) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    // Fetch challenge related details only if it is a self user
    if (principalUser.getId().equals(userId)) {
      user.getChallengeQuestion();
      return mapper.toSecuredDto(user);
    }
    return mapper.toDto(user);
  }

  @Override
  public Page<PartialUserDto> getAll(String filters, Pageable pageable) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    SearchCriteria facilitySearchCriteria = null;
    Long currentFacilityId = principalUser.getCurrentFacilityId();
    if (currentFacilityId != null && !currentFacilityId.equals(Misc.ALL_FACILITY_ID)) {
      facilitySearchCriteria =
        (new SearchCriteria()).setField(User.FACILITY_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(currentFacilityId));
    }
    SearchCriteria mandatorySearchCriteria = (new SearchCriteria()).setField(User.IS_SYSTEM_USER).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(false));
    SearchCriteria organisationSearchCriteria =
      (new SearchCriteria()).setField(User.ORGANISATION_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(principalUser.getOrganisationId()));
    Specification<User> specification = SpecificationBuilder.createSpecification(filters, Arrays.asList(mandatorySearchCriteria, organisationSearchCriteria, facilitySearchCriteria));
    Page<User> users = repository.findAll(specification, pageable);
    return new PageImpl<>(mapper.toPartialDto(users.getContent()), pageable, users.getTotalElements());
  }

  @Override
  public Page<PartialUserDto> getAll(boolean archived, String filters, Pageable pageable) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    SearchCriteria facilitySearchCriteria = null;
    Long currentFacilityId = principalUser.getCurrentFacilityId();
    if (currentFacilityId != null && !currentFacilityId.equals(Misc.ALL_FACILITY_ID)) {
      facilitySearchCriteria =
        (new SearchCriteria()).setField(User.FACILITY_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(currentFacilityId));
    }
    SearchCriteria mandatorySearchCriteria = (new SearchCriteria()).setField(User.IS_SYSTEM_USER).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(false));
    SearchCriteria archivedSearchCriteria = (new SearchCriteria()).setField(User.ARCHIVED).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(archived));
    SearchCriteria organisationSearchCriteria =
      (new SearchCriteria()).setField(UserAudit.ORGANISATION_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(principalUser.getOrganisationId()));
    Specification<User> specification = SpecificationBuilder.createSpecification(filters, Arrays.asList(mandatorySearchCriteria, organisationSearchCriteria, facilitySearchCriteria,
      archivedSearchCriteria));
    Page<User> users = repository.findAll(specification, pageable);
    return new PageImpl<>(mapper.toPartialDto(users.getContent()), pageable, users.getTotalElements());
  }

  @Override
  public Page<PartialFetchUserDto> getAllByRoles(List<String> roles, String filters, boolean isAssigned, List<Object> assignees, Pageable pageable) {
    var principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    SearchCriteria facilitySearchCriteria = null;
    Long currentFacilityId = principalUser.getCurrentFacilityId();
    if (currentFacilityId != null && !currentFacilityId.equals(Misc.ALL_FACILITY_ID)) {
      facilitySearchCriteria =
        (new SearchCriteria()).setField(User.FACILITY_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(currentFacilityId));
    }
    var mandatorySearchCriteria = (new SearchCriteria()).setField(User.IS_SYSTEM_USER).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(false));
    var archivedSearchCriteria = (new SearchCriteria()).setField(User.ARCHIVED).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(false));
    var verifiedSearchCriteria = (new SearchCriteria()).setField(User.STATE).setOp(SearchOperator.IN.toString()).setValues(State.VALID_REGISTERED_STATES);
    var organisationSearchCriteria =
      (new SearchCriteria()).setField(User.ORGANISATION_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(principalUser.getOrganisationId()));
    var serviceSearchCriteria =
      (new SearchCriteria()).setField(User.SERVICE_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(principalUser.getServiceId()));
    var rolesSearchCriteria = (new SearchCriteria()).setField(User.ROLE_NAME).setOp(SearchOperator.IN.toString()).setValues(Collections.singletonList(roles));

    List<SearchCriteria> searchCriteriaList = new ArrayList<>(Arrays.asList(facilitySearchCriteria, mandatorySearchCriteria,
      archivedSearchCriteria, verifiedSearchCriteria, organisationSearchCriteria, serviceSearchCriteria, rolesSearchCriteria));

    SearchCriteria assigneesSearchCriteria;
    if (isAssigned) {
      assigneesSearchCriteria = (new SearchCriteria()).setField(User.ID).setOp(SearchOperator.IN.toString()).setValues(assignees);
      searchCriteriaList.add(assigneesSearchCriteria);
    }

    Specification<User> specification = SpecificationBuilder.createSpecification(filters, searchCriteriaList);
    if (!pageable.getSort().isSorted()) {
    }
    Page<User> pageableUsers = repository.findAll(specification, pageable);
    // TODO getTotalElements incorrect
    return new PageImpl<>(mapper.toPartialFetchDto(pageableUsers.getContent()), pageable, pageableUsers.getTotalElements());
//    List<User> users = pageableUsers.stream().distinct().collect(Collectors.toList());
//    return new PageImpl<>(mapper.toPartialFetchDto(users), pageable, pageableUsers.getTotalElements());
  }


  @Override
  public PrincipalUser archive(Long userId, UserArchiveRequest userArchiveRequest) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    if (user.isArchived()) {
      throw new JaasException(ErrorCode.USER_ALREADY_ARCHIVED);
    } else if (principalUser.getId().equals(user.getId())) {
      throw new JaasException(ErrorCode.USER_CANNOT_ARCHIVE_SELF);
    }
    Set<Long> roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
    if (roleIds.stream().anyMatch(Misc.ACCOUNT_OWNER_IDS::contains)) {
      throw new JaasException(ErrorCode.USER_ACCOUNT_OWNER_CANNOT_BE_ARCHIVED);
    }
    user.setArchived(true);
    user = repository.save(user);
    tokenService.invalidateJtiToken(userId);
    userAuditService.archive(mapper.toPartialUserForAudit(user), userArchiveRequest.getReason(), principalUser);
    return mapper.toPrincipalUser(user);
  }

  @Override
  public PrincipalUser unarchive(Long userId, UserUnarchiveRequest userUnarchiveRequest) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    user.setArchived(false);
    user = repository.save(user);
    userAuditService.unarchive(mapper.toPartialUserForAudit(user), userUnarchiveRequest.getReason(), principalUser);
    return mapper.toPrincipalUser(user);
  }

  @Override
  public PrincipalUser unlock(Long userId) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    State.User currentState = user.getState();
    if (currentState == State.User.UNREGISTERED_LOCKED) {
      user.setState(State.User.UNREGISTERED).setLockedAt(null).setFailedAdditionalVerificationAttempts(0);
    } else if (currentState == State.User.REGISTERED_LOCKED) {
      user.setState(State.User.REGISTERED).setLockedAt(null).setFailedLoginAttempts(0);
    } else if (currentState == State.User.ACCOUNT_LOCKED) {
      user.setState(State.User.REGISTERED).setLockedAt(null).setFailedChallengeQuestionAttempts(0);
    }
    user = repository.save(user);
    userAuditService.unlock(mapper.toPartialUserForAudit(user), principalUser);
    return mapper.toPrincipalUser(user);
  }


  @Override
  public List<PartialUserDto> searchUsers(String query, int limit) throws JsonProcessingException {
    log.info("[searchUsers] Request to search users, query [{}], limit [{}]", query, limit);

    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User userPrincipalEntity = repository.findById(principalUser.getId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));

    JsonNode extras = getExtrasOfOrganisation(userPrincipalEntity);
    switch (getSSOTypeOfOrganisation(extras)) {
      case "OKTA" -> {
        OAuth2ProviderFactory oAuth2ProviderFactory = new OAuth2ProviderFactory(new OktaHandler(getOktaProperties(extras)), null);
        IOauth2Provider oktaProvider = oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.OKTA);
        return oktaProvider.searchUsers(query, limit);
      }
      case "AZURE" -> {
        AzureOauth2Properties azureOauth2Properties = getAzureProperties(userPrincipalEntity);
        OAuth2ProviderFactory oAuth2ProviderFactory = new OAuth2ProviderFactory(null,
          new AzureHandler(azureOauth2Properties, new AzureADGraphConfig(azureOauth2Properties)));

        return oAuth2ProviderFactory.getOAuth2Provider(Misc.UserType.AZURE).searchUsers(query, limit);
      }
      default -> throw new JaasException(ErrorCode.SSO_NOT_CONFIGURED);
    }
  }


  @Override
  public TokenDto switchFacility(Long userId, Long facilityId) {
    var principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var user = repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Message.Error.RESOURCE_NOT_FOUND));
    user.setCurrentFacilityId(facilityId != -1 ? facilityId : null);
    user.setServiceId(principalUser.getServiceId());
    String facilityName = facilityId == -1 ? "Global Portal" : principalUser.getFacilities().stream().filter(f -> f.getId().equals(String.valueOf(facilityId))).findFirst().get().getName();
    userAuditService.switchFacility(mapper.toPartialUserForAudit(user), facilityName);
    var tokenDto = new TokenDto();
    user.setMyMappedFacilities(facilityMapper.toEntityFromPartialFacilityDto(principalUser.getFacilities()));
    tokenDto.setAccessToken(tokenService.generateAccessToken(user));
    return tokenDto;
  }

  private boolean validateSameConfirmPasswordAndPasswordPolicy(String confirmPassword, String password) {
    return confirmPassword.equals(password);
  }

  private boolean validateMinimumPasswordLengthPolicy(String password, int minimumPasswordLength) {
    // Minimum password length policy
    return minimumPasswordLength <= password.length();
  }

  private boolean validatePasswordSimilarToUsernameOrEmailPolicy(String newPassword, String username, String email, boolean allowPasswordSimilarToUsernameOrEmail) {
    // Similar to username or email address policy
    if (!allowPasswordSimilarToUsernameOrEmail) {
      return !(newPassword.equalsIgnoreCase(username) || newPassword.equalsIgnoreCase(email));
    }
    return true;
  }

  private boolean validateMinimumNumericCharactersPolicy(char[] passwordArray, Integer minimumNumericCharacters) {
    // Minimum numeric characters policy
    int numberOfNumericCharacters = 0;
    for (char c : passwordArray) {
      if (Arrays.binarySearch(NUMERIC_CHARACTERS, c) >= 0) {
        numberOfNumericCharacters++;
      }
    }
    return minimumNumericCharacters <= numberOfNumericCharacters;
  }

  private boolean validateMinimumSpecialCharactersPolicy(char[] passwordArray, Integer minimumSpecialCharacters) {
    // Minimum special characters policy
    int numberOfSpecialcaseCharacters = 0;
    for (char c : passwordArray) {
      if (Arrays.binarySearch(SPECIAL_CHARACTERS, c) >= 0) {
        numberOfSpecialcaseCharacters++;
      }
    }
    return minimumSpecialCharacters <= numberOfSpecialcaseCharacters;
  }

  private boolean validateMinimumUppercaseCharactersPolicy(char[] passwordArray, Integer minimumUppercaseCharacters) {
    // Minimum uppercase characters policy
    int numberOfUppercaseCharacters = 0;
    for (char c : passwordArray) {
      if (Arrays.binarySearch(UPPERCASE_CHARACTERS, c) >= 0) {
        numberOfUppercaseCharacters++;
      }
    }
    return minimumUppercaseCharacters <= numberOfUppercaseCharacters;
  }

  private boolean validateMinimumLowercaseCharactersPolicy(char[] passwordArray, Integer minimumLowercaseCharacters) {
    // Minimum lowercase characters policy
    int numberOfLowercaseCharacters = 0;
    for (char c : passwordArray) {
      if (Arrays.binarySearch(LOWERCASE_CHARACTERS, c) >= 0) {
        numberOfLowercaseCharacters++;
      }
    }
    return minimumLowercaseCharacters <= numberOfLowercaseCharacters;
  }

  private boolean validatePasswordMatchInPreviousPasswordsPolicy(Long userId, String newPassword, int minimumPasswordHistory) {
    // Minimum password history policy
    if (minimumPasswordHistory > 0) {
      Pageable pageable = PageRequest.of(0, minimumPasswordHistory, Sort.by(Sort.Direction.DESC, "createdAt"));
      List<PasswordHistory> passwordHistories = passwordHistoryRepository.findAllByUserId(userId, pageable);
      boolean passwordMatchInPreviousPasswords = false;
      for (PasswordHistory passwordHistory : passwordHistories) {
        if (User.validatePassword(newPassword, passwordHistory.getPassword())) {
          passwordMatchInPreviousPasswords = true;
          break;
        }
      }
      return !passwordMatchInPreviousPasswords;
    }
    return false;
  }

  private boolean validateDuplicateUsernameOrEmail(String usernameOrEmail) {
    if (repository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).isPresent()) {
      throw new DuplicateException(ErrorCode.USER_WITH_USERNAME_OR_EMAIL_ALREADY_EXISTS);
    }
    return true;
  }

  @Override
  public BasicDto isUsernameAvailable(String username) {
    validateDuplicateUsernameOrEmail(username);
    return BasicDto.builder().message(Message.USERNAME_AVAILABLE).build();
  }

  @Override
  public BasicDto isEmailAvailable(String email) {
    validateDuplicateUsernameOrEmail(email);
    return BasicDto.builder().message(Message.EMAIL_AVAILABLE).build();
  }

  @Override
  public BasicDto isEmployeeIdAvailable(String employeeId) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    validateDuplicateEmployeeId(employeeId, principalUser.getOrganisationId());
    return BasicDto.builder().message(Message.EMPLOYEE_ID_AVAILABLE).build();
  }

  @Override
  @Transactional
  public PrincipalUser update(UserUpdateRequest userUpdateRequest) {
    var principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Utility.isEmpty(userUpdateRequest.getFacilities())) {
      throw new JaasException(Message.Error.ATLEAST_ONE_FACILITY_REQUIRED);
    }
    User user = repository.findById(userUpdateRequest.getId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));

    if (user.isArchived()) {
      throw new JaasException(ErrorCode.USER_IS_ARCHIVED);
    }
    Set<Long> roleIds = userUpdateRequest.getRoles().stream().map(r -> Long.valueOf(r.getId())).collect(Collectors.toSet());
    checkSelfRoleUpdate(principalUser, user.getId(), roleIds);
    UserDto oldDto = mapper.toDto(user);
    mapper.update(userUpdateRequest, user);

    Set<Long> facilityIds = userUpdateRequest.getFacilities().stream().map(f -> Long.valueOf(f.getId())).collect(Collectors.toSet());
    List<Facility> facilities = facilityRepository.findAllById(facilityIds);
    if (Utility.isEmpty(facilities)) {
      if (!(principalUser.getServiceId().equals(Misc.Services.DWI.ID) && roleIds.stream().anyMatch(Misc.Services.DWI.ALL_FACILITY_ROLES::contains))) {
        throw new JaasException(Message.Error.ATLEAST_ONE_FACILITY_REQUIRED);
      } else {
        facilities = facilityRepository.findAllByOrganisationId(principalUser.getOrganisationId());
      }
    }
    checkPrincipalUserHasPermissionToUpdateUser(principalUser);
    user.setEmail(userUpdateRequest.getEmail());
    user.setFacilities(facilities);
    user.setRoles(roleIds.stream().map(roleRepository::getOne).collect(Collectors.toList()));
    repository.save(user);
    UserDto newDto = mapper.toDto(user);
    userAuditService.updateUser(oldDto, newDto, userUpdateRequest.getReason(), mapper.toPartialUserForAudit(user), principalUser);
    return mapper.toPrincipalUser(user);
  }

  @Override
  @Transactional
  public PrincipalUser updateBasicInformation(UserBasicInformationUpdateRequest userBasicInformationUpdateRequest) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = repository.findById(userBasicInformationUpdateRequest.getId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    UserDto oldDto = mapper.toDto(user);
    mapper.update(userBasicInformationUpdateRequest, user);
    UserDto newDto = mapper.toDto(user);
    user.setEmail(userBasicInformationUpdateRequest.getEmail());
    user.setModifiedBy(repository.getOne(principalUser.getId()));
    user = repository.save(user);
    userAuditService.updateUser(oldDto, newDto, userBasicInformationUpdateRequest.getReason(), mapper.toPartialUserForAudit(user), principalUser);
    return mapper.toPrincipalUser(user);
  }

  @Override
  public ChallengeQuestionDto getChallengeQuestionsAnswer(Long userId, String token) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!principalUser.getId().equals(userId)) {
      throw new JaasException(ErrorCode.ACCESS_DENIED);
    }
    ValidationToken validationToken = tokenService.extractValidationToken(token).orElseThrow(() -> new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID));
    if (!validationToken.getPurposes().contains(Misc.ValidationPurpose.CHALLENGE_QUESTION_UPDATE)) {
      throw new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID);
    }
    User user = repository.findById(principalUser.getId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    ChallengeQuestionDto challengeQuestionDto = challengeQuestionMapper.toDto(user.getChallengeQuestion());
    challengeQuestionDto.setAnswer(user.getChallengeQuestionsAnswer());
    return challengeQuestionDto;
  }

  @Override
  @Transactional
  public BasicDto updateChallengeQuestionsAnswer(ChallengeQuestionsAnswerUpdateRequest challengeQuestionsAnswerUpdateRequest) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = repository.findById(principalUser.getId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    if (!State.USER_VALID_REGISTERED_STATES.contains(user.getState())) {
      throw new JaasException(ErrorCode.USER_NOT_REGISTERED);
    }
    ValidationToken validationToken = tokenService.extractValidationToken(challengeQuestionsAnswerUpdateRequest.getToken()).orElseThrow(() -> new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID));
    if (!validationToken.getPurposes().contains(Misc.ValidationPurpose.CHALLENGE_QUESTION_UPDATE) || !principalUser.getId().equals(Long.valueOf(validationToken.getIdentifier()))) {
      throw new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID);
    }
    ChallengeQuestion challengeQuestion = challengeQuestionRepository.findById(challengeQuestionsAnswerUpdateRequest.getId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.RESOURCE_NOT_FOUND));
    user.setChallengeQuestion(challengeQuestion)
      .setChallengeQuestionsAnswer(challengeQuestionsAnswerUpdateRequest.getAnswer());
    repository.save(user);
    userAuditService.updateChallengeQuestionsAnswer(mapper.toPartialUserForAudit(user));
    return BasicDto.builder().id(String.valueOf(user.getId())).message(Message.SUCCESS).build();
  }

  @Override
  public PrincipalUser resetToken(Long userId) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    State.User state = user.getState();
    // TODO validate user
    if (State.USER_VALID_REGISTERED_STATES.contains(state)) {
      String passwordResetToken = tokenService.generatePasswordResetToken(user);
      user.setToken(passwordResetToken);
      sendEmail(user, passwordResetToken, Action.Email.TOKEN_RESET, principalUser.getServiceId());
      userAuditService.resetToken(mapper.toPartialUserForAudit(user), principalUser);
    } else if (state == State.User.UNREGISTERED || state == State.User.INVITE_CANCELLED || state == State.User.INVITE_EXPIRED) {
      String registrationToken = tokenService.generateRegistrationToken(user);
      user.setFailedAdditionalVerificationAttempts(0);
      user.setToken(registrationToken);
      user.setState(State.User.UNREGISTERED);
      repository.save(user);
      userAuditService.resetToken(mapper.toPartialUserForAudit(user), principalUser);
      sendEmail(user, registrationToken, Action.Email.RESEND_INVITE, principalUser.getServiceId());
    } else if (State.USER_LOCKED_STATES.contains(state)) {
      throw new JaasException(ErrorCode.USER_LOCKED);
    } else {
      throw new JaasException(ErrorCode.UNKNOWN);
    }
    return mapper.toPrincipalUser(user);
  }

  @Override
  public PrincipalUser cancelToken(Long userId) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    if (State.USER_VALID_REGISTERED_STATES.contains(user.getState())) {
      tokenService.cancelPasswordResetToken(user);
    } else {
      tokenService.cancelRegistrationToken(user);
      user.setState(State.User.INVITE_CANCELLED).setArchived(true).setModifiedById(principalUser.getId());
      repository.save(user);
      userAuditService.cancelInvite(mapper.toPartialUserForAudit(user), principalUser);
      userAuditService.archive(mapper.toPartialUserForAudit(user), "", principalUser);
    }
    return mapper.toPrincipalUser(user);
  }

  @Override
  @Transactional
  public BasicDto updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = repository.findById(principalUser.getId()).orElseThrow(() -> new ResourceNotFoundException(Message.Error.USER_NOT_FOUND));
    if (!principalUser.getId().equals(passwordUpdateRequest.getUserId())) {
      throw new JaasException(ErrorCode.ACCESS_DENIED);
    }
    ValidationToken validationToken = tokenService.extractValidationToken(passwordUpdateRequest.getToken()).orElseThrow(() -> new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID));
    if (!validationToken.getPurposes().contains(Misc.ValidationPurpose.PASSWORD_UPDATE) || !principalUser.getId().equals(Long.valueOf(validationToken.getIdentifier()))) {
      throw new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID);
    }
    List<IErrorCode> errors = validatePasswordAgainstPasswordPolicy(user, user.getUsername(), passwordUpdateRequest.getConfirmPassword(), passwordUpdateRequest.getPassword());
    if (!Utility.isEmpty(errors)) {
      throw new JaasException(errors);
    }
    if (user.getState().equals(State.User.PASSWORD_EXPIRED)) {
      user.setState(State.User.REGISTERED);
    }
    // TODO add validation
    user.setPassword(passwordUpdateRequest.getPassword()).setPasswordUpdatedAt(DateTimeUtils.now());
    user = repository.save(user);
    PasswordHistory passwordHistory = new PasswordHistory();
    passwordHistory.setUser(user).setPassword(passwordUpdateRequest.getPassword());
    passwordHistoryRepository.save(passwordHistory);
    userAuditService.updatePassword(mapper.toPartialUserForAudit(user));
    return BasicDto.builder().id(String.valueOf(user.getId())).message(Message.PASSWORD_UPDATED).build();
  }

  @Override
  public List<IErrorCode> validatePasswordAgainstPasswordPolicy(User user, String username, String confirmPassword, String password) {
    List<IErrorCode> errors = new ArrayList<>();
    PasswordPolicy passwordPolicy = user.getOrganisation().getPasswordPolicy();

    if (!validateSameConfirmPasswordAndPasswordPolicy(confirmPassword, password)) {
      errors.add(ErrorCode.PASSWORD_POLICY_CONFIRM_PASSWORD_AND_PASSWORD_ARE_NOT_SAME);
    }

    if (!validateMinimumPasswordLengthPolicy(password, passwordPolicy.getMinimumPasswordLength())) {
      errors.add(new CustomErrorCode(ErrorCode.PASSWORD_POLICY_TOO_SHORT.code(),
        MessageFormat.format(ErrorCode.PASSWORD_POLICY_TOO_SHORT.message(), passwordPolicy.getMinimumPasswordLength())));
    }

    char[] passwordArray = password.toCharArray();

    if (!validateMinimumLowercaseCharactersPolicy(passwordArray, passwordPolicy.getMinimumLowercaseCharacters())) {
      errors.add(new CustomErrorCode(ErrorCode.PASSWORD_POLICY_INSUFFICIENT_LOWERCASE.code(),
        MessageFormat.format(ErrorCode.PASSWORD_POLICY_INSUFFICIENT_LOWERCASE.message(), passwordPolicy.getMinimumLowercaseCharacters())));
    }

    if (!validateMinimumUppercaseCharactersPolicy(passwordArray, passwordPolicy.getMinimumUppercaseCharacters())) {
      errors.add(new CustomErrorCode(ErrorCode.PASSWORD_POLICY_INSUFFICIENT_UPPERCASE.code(),
        MessageFormat.format(ErrorCode.PASSWORD_POLICY_INSUFFICIENT_UPPERCASE.message(), passwordPolicy.getMinimumUppercaseCharacters())));
    }

    if (!validateMinimumSpecialCharactersPolicy(passwordArray, passwordPolicy.getMinimumSpecialCharacters())) {
      errors.add(new CustomErrorCode(ErrorCode.PASSWORD_POLICY_INSUFFICIENT_SPECIAL_CHARACTERS.code(),
        MessageFormat.format(ErrorCode.PASSWORD_POLICY_INSUFFICIENT_SPECIAL_CHARACTERS.message(), passwordPolicy.getMinimumSpecialCharacters())));
    }

    if (!validateMinimumNumericCharactersPolicy(passwordArray, passwordPolicy.getMinimumNumericCharacters())) {
      errors.add(new CustomErrorCode(ErrorCode.PASSWORD_POLICY_INSUFFICIENT_NUMERIC_CHARACTERS.code(),
        MessageFormat.format(ErrorCode.PASSWORD_POLICY_INSUFFICIENT_NUMERIC_CHARACTERS.message(), passwordPolicy.getMinimumNumericCharacters())));
    }

    if (!validatePasswordSimilarToUsernameOrEmailPolicy(password, username, user.getEmail(), passwordPolicy.isAllowPasswordSimilarToUsernameOrEmail())) {
      errors.add(ErrorCode.PASSWORD_POLICY_SIMILAR_TO_USERNAME_OR_EMAIL);
    }

    if (!validatePasswordMatchInPreviousPasswordsPolicy(user.getId(), password, passwordPolicy.getMinimumPasswordHistory())) {
      errors.add(new CustomErrorCode(ErrorCode.PASSWORD_POLICY_HISTORY_VIOLATION.code(),
        MessageFormat.format(ErrorCode.PASSWORD_POLICY_HISTORY_VIOLATION.message(), passwordPolicy.getMinimumPasswordHistory())));
    }
    return errors;
  }


  private boolean validateUser(User user) {
    if (null != user) {
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
        case PASSWORD_EXPIRED:
          throw new ForbiddenException(ErrorCode.PASSWORD_EXPIRED);
      }
      if (user.isArchived()) {
        throw new ForbiddenException(ErrorCode.USER_ARCHIVED);
      }
      return true;
    }
    return false;
  }

  private OktaOAuth2Properties getOktaProperties(JsonNode extras) throws JsonProcessingException {
    return JsonUtils.readValue(JsonUtils.writeValueAsString(extras.get("okta")), OktaOAuth2Properties.class);
  }

  private JsonNode getExtrasOfOrganisation(User userPrincipalEntity) {
    OrganisationSetting organisationSetting = this.organisationSettingRepository.findByOrganisationId(userPrincipalEntity.getOrganisationId());
    return organisationSetting.getExtras();
  }

  @NotNull
  private String getSSOTypeOfOrganisation(JsonNode extras) {
    if (!Utility.isEmpty(extras)) {
      return extras.fieldNames().next().toUpperCase();
    } else {
      throw new JaasException(ErrorCode.SSO_NOT_CONFIGURED);
    }
  }

  private AzureOauth2Properties getAzureProperties(User user) throws JsonProcessingException {
    OrganisationSetting organisationSetting = this.organisationSettingRepository.findByOrganisationId(user.getOrganisationId());
    JsonNode extras = organisationSetting.getExtras();
    return JsonUtils.readValue(JsonUtils.writeValueAsString(extras.get("azure")), AzureOauth2Properties.class);
  }

  private void checkPrincipalUserHasPermissionToUpdateUser(PrincipalUser principalUser) {
    Set<String> principalUserRoleNames = principalUser.getRoleNames();
    if (!(principalUserRoleNames.contains(Misc.Services.DWI.Roles.SYSTEM_ADMIN.name()) || principalUserRoleNames.contains(Misc.Services.DWI.Roles.ACCOUNT_OWNER.name()))) {
      throw new JaasException(ErrorCode.ACTION_DENIED);
    }
  }

  private void checkSelfRoleUpdate(PrincipalUser principalUser,Long userId, Set<Long> roleIds) {
    Set<Long> principalUserRoleIds = principalUser.getRoles().stream().map(r -> Long.valueOf(r.getId())).collect(Collectors.toSet());
    if (principalUser.getId().equals(userId) && !principalUserRoleIds.equals(roleIds)) {
      throw new JaasException(ErrorCode.ACTION_DENIED);
    }
  }
}
