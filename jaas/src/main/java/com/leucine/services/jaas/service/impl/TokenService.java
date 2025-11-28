package com.leucine.services.jaas.service.impl;

import com.leucine.services.jaas.repository.IFacilityRepository;
import com.leucine.services.jaas.util.UUIDGenerator;
import com.leucine.services.jaas.config.JwtConfiguration;
import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.constant.Type;
import com.leucine.services.jaas.dto.AuthenticationDto;
import com.leucine.services.jaas.dto.ValidationToken;
import com.leucine.services.jaas.dto.partial.PartialFacilityDto;
import com.leucine.services.jaas.dto.partial.PartialRoleDto;
import com.leucine.services.jaas.exception.ForbiddenException;
import com.leucine.services.jaas.exception.InvalidTokenException;
import com.leucine.services.jaas.exception.JaasException;
import com.leucine.services.jaas.exception.TokenRevokedException;
import com.leucine.services.jaas.model.*;
import com.leucine.services.jaas.model.helper.BaseEntity;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import com.leucine.services.jaas.repository.IClientCredentialRepository;
import com.leucine.services.jaas.repository.ITokenRepository;
import com.leucine.services.jaas.service.ITokenService;
import com.leucine.services.jaas.util.DateTimeUtils;
import com.leucine.services.jaas.util.Utility;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  private static final String SOURCE = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  private static final String KEY_ID = "id";
  private static final String KEY_EMPLOYEE_ID = "employeeId";
  private static final String KEY_FIRST_NAME = "firstName";
  private static final String KEY_LAST_NAME = "lastName";
  private static final String KEY_EMAIL = "email";
  private static final String KEY_USERNAME = "username";
  private static final String KEY_SERVICES_ID = "serviceId";
  private static final String KEY_ORGANISATIONS_ID = "organisationId";
  private static final String KEY_HAS_SET_CHALLENGE_QUESTION = "hasSetChallengeQuestion";
  private static final String KEY_FACILITIES = "facilities";
  private static final String KEY_FACILITY_IDS = "facilityIds";
  private static final String KEY_CURRENT_FACILITY_ID = "currentFacilityId";
  //  private static final String KEY_ROLE_IDS = "roleIds";
  private static final String KEY_ROLES = "roles";
  private static final String KEY_ROLE_NAMES = "roleNames";
  private static final String KEY_JTI = "jti";
  private static final String KEY_SCOPES = "scopes";

  private final ITokenRepository repository;
  private final JwtConfiguration jwtConfiguration;
  private final IClientCredentialRepository clientCredentialRepository;
  private final IFacilityRepository facilityRepository;

  @Override
  @Transactional
  public Object[] generateAccessAndRefreshToken(User user) {
    validateUser(user);
    long accessTokenExpiration = DateTimeUtils.nowPlusMinutesToEpochMilli(jwtConfiguration.getAccessTokenExpirationInMinutes());
    long refreshTokenExpiration = DateTimeUtils.nowPlusMinutesToEpochMilli(jwtConfiguration.getRefreshTokenExpirationInMinutes());
    Long userId = user.getId();
    String jti = generateJtiToken(userId, jwtConfiguration.getRefreshTokenExpirationInMinutes());
    Map<String, Object> accessClaims = generateAccessClaim(user, jti);
    String accessToken = generateToken(user.getId().toString(), accessClaims, user.getUsername(), new Date(accessTokenExpiration));
    Map<String, Object> refreshClaims = generateRefreshClaim(user, jti);
    String refreshToken = generateToken(user.getId().toString(), refreshClaims, user.getUsername(), new Date(refreshTokenExpiration));
    return new Object[]{accessToken, refreshToken};
  }

  @Override
  @Transactional
  public Object[] generateAccessAndRefreshToken(User user, String token) {
    validateUser(user);
    PrincipalUser principalUser = forcefullyGetPrincipalUser(token);
    long accessTokenExpiration = DateTimeUtils.nowPlusMinutesToEpochMilli(jwtConfiguration.getAccessTokenExpirationInMinutes());
    long refreshTokenExpiration = DateTimeUtils.nowPlusMinutesToEpochMilli(jwtConfiguration.getRefreshTokenExpirationInMinutes());
    Long userId = user.getId();
    String jti = generateJtiToken(userId, jwtConfiguration.getRefreshTokenExpirationInMinutes());
    Map<String, Object> accessClaims = generateAccessClaim(principalUser, jti);
//    accessClaims.put(KEY_FACILITIES, user.getMyMappedFacilities().stream().map(f -> (new PartialFacilityDto()).setId(f.getIdAsString()).setName(f.getName())).collect(Collectors.toList()));
//    accessClaims.put(KEY_ROLES, user.getRoles().stream().map(r -> (new PartialRoleDto()).setId(r.getIdAsString()).setName(r.getName())).collect(Collectors.toList()));
    String accessToken = generateToken(user.getId().toString(), accessClaims, user.getUsername(), new Date(accessTokenExpiration));
    Map<String, Object> refreshClaims = generateRefreshClaim(user, jti);
    String refreshToken = generateToken(user.getId().toString(), refreshClaims, user.getUsername(), new Date(refreshTokenExpiration));
    return new Object[]{accessToken, refreshToken, principalUser};
  }

  private Map<String, Object> generateAccessClaim(User user, String jti) {
    Map<String, Object> accessClaims = new HashMap<>(15);
    accessClaims.put(KEY_ID, user.getId());
    accessClaims.put(KEY_EMPLOYEE_ID, user.getEmployeeId());
    accessClaims.put(KEY_FIRST_NAME, user.getFirstName());
    accessClaims.put(KEY_LAST_NAME, user.getLastName());
    accessClaims.put(KEY_EMAIL, user.getEmail());
    accessClaims.put(KEY_USERNAME, user.getUsername());
    accessClaims.put(KEY_ORGANISATIONS_ID, user.getOrganisationId());
    accessClaims.put(KEY_SERVICES_ID, user.getServiceId());
    accessClaims.put(KEY_HAS_SET_CHALLENGE_QUESTION, user.isHasSetChallengeQuestion());
    accessClaims.put(KEY_FACILITIES, user.getMyMappedFacilities().stream().map(f -> (new PartialFacilityDto()).setId(f.getIdAsString()).setName(f.getName())).collect(Collectors.toList()));
    accessClaims.put(KEY_FACILITY_IDS, user.getMyMappedFacilities().stream().map(BaseEntity::getId).collect(Collectors.toSet()));
    accessClaims.put(KEY_CURRENT_FACILITY_ID, user.getCurrentFacilityId());
    accessClaims.put(KEY_ROLES, user.getRoles().stream().map(r -> (new PartialRoleDto()).setId(r.getIdAsString()).setName(r.getName())).collect(Collectors.toList()));
    accessClaims.put(KEY_ROLE_NAMES, user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    accessClaims.put(KEY_JTI, jti);
    return accessClaims;
  }

  private Map<String, Object> generateAccessClaim(PrincipalUser principalUser, String jti) {
    Map<String, Object> accessClaims = new HashMap<>(15);
    accessClaims.put(KEY_ID, principalUser.getId());
    accessClaims.put(KEY_EMPLOYEE_ID, principalUser.getEmployeeId());
    accessClaims.put(KEY_FIRST_NAME, principalUser.getFirstName());
    accessClaims.put(KEY_LAST_NAME, principalUser.getLastName());
    accessClaims.put(KEY_EMAIL, principalUser.getEmail());
    accessClaims.put(KEY_USERNAME, principalUser.getUsername());
    accessClaims.put(KEY_SERVICES_ID, principalUser.getServiceId());
    accessClaims.put(KEY_ORGANISATIONS_ID, principalUser.getOrganisationId());
    accessClaims.put(KEY_HAS_SET_CHALLENGE_QUESTION, principalUser.isHasSetChallengeQuestion());
    accessClaims.put(KEY_FACILITIES, principalUser.getFacilities());
    accessClaims.put(KEY_FACILITY_IDS, principalUser.getFacilityIds());
    accessClaims.put(KEY_CURRENT_FACILITY_ID, principalUser.getCurrentFacilityId());
    accessClaims.put(KEY_ROLES, principalUser.getRoles());
    accessClaims.put(KEY_ROLE_NAMES, principalUser.getRoleNames());
    accessClaims.put(KEY_JTI, jti);
    return accessClaims;
  }

  private Map<String, Object> generateRefreshClaim(User user, String jti) {
    Map<String, Object> refreshClaims = new HashMap<>(3);
    refreshClaims.put(KEY_ID, user.getId());
    refreshClaims.put(KEY_JTI, jti);
    refreshClaims.put(KEY_SCOPES, Collections.singletonList("REFRESH_TOKEN"));
    return refreshClaims;
  }

  private boolean validateUser(User user) {
    // TODO see if we can have a common method for validating user before registration and after registration
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

  @Transactional
  String generateJtiToken(Long userId, Integer expiration) {
    String jtiToken = UUIDGenerator.getInstance().generateUnique();
    invalidatePreviousJtiTokens(userId);
    Token token = new Token();
    token.setToken(jtiToken)
      .setType(Type.Token.JTI)
      .setUserId(userId)
      .setExpiration(DateTimeUtils.nowPlusMinutesToEpochSeconds(expiration));
    repository.save(token);
    return jtiToken;
  }

  private String generateToken(String id, Map<String, Object> claims, String subject, Date expirationDate) {
    return Jwts.builder()
      .setId(id)
      .setClaims(claims)
      .setSubject(subject)
      .setIssuedAt(new Date())
      .setExpiration(expirationDate)
      .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(jwtConfiguration.getSecret().getBytes()))
      .compact();
  }

  private boolean invalidatePreviousJtiTokens(Long userId) {
    return repository.deleteByUserIdAndType(userId, Type.Token.JTI) == 1;
  }

  @Override
  @Transactional
  public String generateAccessToken(User user) {
    validateUser(user);
    long accessTokenExpiration = DateTimeUtils.nowPlusMinutesToEpochMilli(jwtConfiguration.getAccessTokenExpirationInMinutes());
    Long userId = user.getId();
    String jti = generateJtiToken(userId, jwtConfiguration.getRefreshTokenExpirationInMinutes());
    Map<String, Object> accessClaims = generateAccessClaim(user, jti);
    return generateToken(user.getId().toString(), accessClaims, user.getUsername(), new Date(accessTokenExpiration));
  }


  @Override
  @Transactional
  public String generateAccessToken(PrincipalUser principalUser, User user) {
    validateUser(user);
    long accessTokenExpiration = DateTimeUtils.nowPlusMinutesToEpochMilli(jwtConfiguration.getAccessTokenExpirationInMinutes());
    Long usersId = principalUser.getId();
    String jti = generateJtiToken(usersId, jwtConfiguration.getRefreshTokenExpirationInMinutes());
    Map<String, Object> accessClaims = generateAccessClaim(principalUser, jti);
    return generateToken(principalUser.getId().toString(), accessClaims, principalUser.getUsername(), new Date(accessTokenExpiration));
  }

  @Override
  @Transactional
  public String generateRegistrationToken(User user) {
    Long userId = user.getId();
    Integer expiration = user.getOrganisation().getSettings().getRegistrationTokenExpiration();
    String registrationToken = getSecretKey(jwtConfiguration.getSecretKeyLength());
    invalidatePreviousRegistrationTokens(userId);
    Token token = new Token();
    token.setToken(registrationToken)
      .setType(Type.Token.REGISTRATION)
      .setUserId(userId)
      .setExpiration(DateTimeUtils.nowPlusMinutesToEpochSeconds(expiration));
    repository.save(token);
    return registrationToken;
  }

  private String getSecretKey(final int length) {
    String secretKey = randomString(length);
    int secretKeyGenerationLimit = 10;
    while (secretKeyGenerationLimit > 0 && repository.existsByToken(secretKey)) {
      secretKey = randomString(length);
      secretKeyGenerationLimit--;
    }
    if (secretKeyGenerationLimit <= 0) {
      throw new JaasException(ErrorCode.SECRET_KEY_GENERATION_LIMIT_EXCEEDED);
    }
    return secretKey;
  }

  private void invalidatePreviousRegistrationTokens(Long userId) {
    repository.deleteByUserIdAndType(userId, Type.Token.REGISTRATION);
  }

  private String randomString(final int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(SOURCE.charAt(SECURE_RANDOM.nextInt(SOURCE.length())));
    }
    return sb.toString();
  }

  @Override
  @Transactional
  public String generatePasswordResetToken(User user) {
    Long userId = user.getId();
    Integer expiration = user.getOrganisation().getSettings().getPasswordResetTokenExpiration();
    String passwordResetToken = getSecretKey(jwtConfiguration.getSecretKeyLength());
    invalidatePreviousPasswordResetTokens(userId);
    Token token = new Token();
    token.setToken(passwordResetToken)
      .setType(Type.Token.PASSWORD_RESET)
      .setUserId(userId)
      .setExpiration(DateTimeUtils.nowPlusMinutesToEpochSeconds(expiration));
    repository.save(token);
    return passwordResetToken;
  }

  private void invalidatePreviousPasswordResetTokens(Long userId) {
    repository.deleteByUserIdAndType(userId, Type.Token.PASSWORD_RESET);
  }

  @Override
  public PrincipalUser getPrincipalUser(String token) {
    Claims claims = extractAllClaims(token);
    return getPrincipalUser(token, claims);
  }

  @Override
  public PrincipalUser forcefullyGetPrincipalUser(String token) {
    PrincipalUser principalUser = null;
    try {
      Claims claims = extractAllClaims(token);
      principalUser = getPrincipalUser(token, claims);
    } catch (ExpiredJwtException ex) {
      principalUser = getPrincipalUser(token, ex.getClaims());
    }
    return principalUser;
  }

  @Override
  public PrincipalUser getPrincipalUser(String token, Claims claims) {
    PrincipalUser principalUser = new PrincipalUser();
    List<PartialFacilityDto> facilities = ((ArrayList<HashMap<String, String>>) claims.get(KEY_FACILITIES)).stream().map(f -> (new PartialFacilityDto()).setId(f.get("id")).setName(f.get("name"))).collect(Collectors.toList());
    Set<Long> facilityIds = facilities.stream().map(f -> Long.valueOf(f.getId())).collect(Collectors.toSet());
    List<PartialRoleDto> roles = ((ArrayList<HashMap<String, String>>) claims.get(KEY_ROLES)).stream().map(f -> (new PartialRoleDto()).setId(f.get("id")).setName(f.get("name"))).collect(Collectors.toList());
    Set<String> roleNames = roles.stream().map(PartialRoleDto::getName).collect(Collectors.toSet());
    principalUser.setId(claims.get(KEY_ID, Long.class))
      .setEmployeeId(claims.get(KEY_EMPLOYEE_ID, String.class))
      .setFirstName(claims.get(KEY_FIRST_NAME, String.class))
      .setLastName(claims.get(KEY_LAST_NAME, String.class))
      .setEmail(claims.get(KEY_EMAIL, String.class))
      .setUsername(claims.get(KEY_USERNAME, String.class))
      .setServiceId(claims.get(KEY_SERVICES_ID, String.class))
      .setOrganisationId(claims.get(KEY_ORGANISATIONS_ID, Long.class))
      .setHasSetChallengeQuestion(claims.get(KEY_HAS_SET_CHALLENGE_QUESTION, Boolean.class))
      .setFacilities(facilities)
      .setFacilityIds(facilityIds)
      .setCurrentFacilityId(claims.get(KEY_CURRENT_FACILITY_ID, Long.class))
      .setRoles(roles)
      .setRoleNames(roleNames)
      .setToken(token)
      .setJti(claims.get(KEY_JTI, String.class));
    return principalUser;
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(Base64.getEncoder().encode(jwtConfiguration.getSecret().getBytes())).parseClaimsJws(token).getBody();
  }

  @Override
  @Transactional
  public boolean invalidateJtiToken(Long userId) {
    return invalidatePreviousJtiTokens(userId);
  }

//  @Override
//  @Transactional
//  public boolean invalidateJtiToken(Set<Long> userIds) {
//    return invalidatePreviousJtiTokens(userIds);
//  }
//
//  private boolean invalidatePreviousJtiTokens(Set<Long> userIds) {
//    return Utility.isEmpty(repository.deleteByUserIdInAndType(userIds, Type.Token.JTI));
//  }

  @Override
  public String generateValidationToken(ValidationToken validationToken) {
    return Jwts.builder()
      .setId(validationToken.getIdentifier())
      .setSubject(validationToken.getPurposes().stream().map(Object::toString).collect(Collectors.joining(",")))
      .setIssuedAt(new Date())
      .setExpiration(new Date(DateTimeUtils.nowPlusMinutesToEpochMilli(jwtConfiguration.getValidationTokenExpirationInMinutes())))
      .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(jwtConfiguration.getSecret().getBytes()))
      .compact();
  }

  @Override
  public Optional<ValidationToken> extractValidationToken(String token) {
    ValidationToken validationToken = null;
    if (!Utility.isNull(token)) {
      try {
        Claims claims = extractAllClaims(token);
        validationToken = new ValidationToken()
          .setIdentifier(claims.getId())
          .setPurposes(Arrays.stream(claims.getSubject().split(",", -1)).map(Misc.ValidationPurpose::valueOf).collect(Collectors.toSet()));
      } catch (SignatureException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException ex) {
        throw new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID, ex);
      } catch (ExpiredJwtException ex) {
        throw new InvalidTokenException(ErrorCode.JWT_TOKEN_EXPIRED);
      }
    }
    return Optional.ofNullable(validationToken);
  }

  @Override
  public String getUsername(String token, Type.Token type) {
    return extractClaim(token, type, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Type.Token type, Function<Claims, T> claimResolver) {
    isJwtTokenExpired(token, type);
    Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private boolean isJwtTokenExpired(String token, Type.Token type) {
    try {
      return extractAllClaims(token).getExpiration().before(new Date());
    } catch (SignatureException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException ex) {
      throw new InvalidTokenException(ErrorCode.JWT_TOKEN_INVALID, ex);
    } catch (ExpiredJwtException ex) {
      switch (type) {
        case ACCESS:
          throw new InvalidTokenException(ErrorCode.JWT_ACCESS_TOKEN_EXPIRED, ex);
        case REFRESH:
          throw new InvalidTokenException(ErrorCode.JWT_REFRESH_TOKEN_EXPIRED, ex);
        default:
          throw new InvalidTokenException(ErrorCode.JWT_TOKEN_EXPIRED, ex);
      }
    }
  }

  @Override
  public Token validateAndFetchToken(String token, Type.Token type) {
    if (Utility.isNull(token) || Utility.isNull(type)) {
      throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
    }
    Token tokenEntity = repository.findByTokenAndType(token, type).orElseThrow(() -> new InvalidTokenException(ErrorCode.INVALID_TOKEN));
    if (tokenEntity.getExpiration() < DateTimeUtils.now()) {
//      cleanup(tokenEntity);
      switch (tokenEntity.getType()) {
        case REGISTRATION:
          throw new InvalidTokenException(ErrorCode.REGISTRATION_TOKEN_EXPIRED,
            generateValidationToken(new ValidationToken().setIdentifier(String.valueOf(tokenEntity.getUserId())).addPurpose(Misc.ValidationPurpose.NOTIFY_ADMIN)));
        case PASSWORD_RESET:
          throw new InvalidTokenException(ErrorCode.FORGOT_PASSWORD_TOKEN_EXPIRED,
            generateValidationToken(new ValidationToken().setIdentifier(String.valueOf(tokenEntity.getUserId())).addPurpose(Misc.ValidationPurpose.NOTIFY_ADMIN)));
        default:
          throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
      }
    }
    return tokenEntity;
  }

  @Override
  public boolean cleanup(Token token) {
    if (!Utility.isNull(token)) {
      repository.delete(token);
      return true;
    }
    return false;
  }

  @Override
  public boolean isAuthorized(String token, Type.Token type, HttpServletRequest request) {
    if (StringUtils.hasText(token) && !isJwtTokenExpired(token, type)) {
      if (validateJtiToken(token)) {
//        String path = request.getServletPath();
//        String method = request.getMethod();
        // TODO validate permissions
        return true;
      } else {
        throw new TokenRevokedException(ErrorCode.JTI_TOKEN_REVOKED);
      }
    }
    return false;
  }

  private boolean validateJtiToken(String token) {
    String jti = (String) extractAllClaims(token).get(KEY_JTI);
    Token tokenEntity = repository.findByTokenAndType(jti, Type.Token.JTI).orElse(null);
    return !Utility.isNull(tokenEntity);
  }

  @Override
  @Transactional
  public void cancelRegistrationToken(User user) {
    invalidatePreviousRegistrationTokens(user.getId());
  }

  @Override
  @Transactional
  public AuthenticationDto generateClientAccessToken(User user, String clientId, String clientSecret, String facilityId) {
    List<ClientFacilityMapping> clientFacilityMappings = clientCredentialRepository.findByClientId(clientId);
    Set<Facility> facilities = clientFacilityMappings.stream().map(cfm -> cfm.getClientFacilityCompositeId().getFacility()).collect(Collectors.toSet());
    long accessTokenExpiration = DateTimeUtils.nowPlusMinutesToEpochMilli(jwtConfiguration.getAccessTokenExpirationInMinutes());
    long refreshTokenExpiration = DateTimeUtils.nowPlusMinutesToEpochMilli(jwtConfiguration.getRefreshTokenExpirationInMinutes());

    Facility currentFacility = facilityRepository.getReferenceById(Long.valueOf(facilityId));
    Long organisationId = currentFacility.getOrganisationId();

    Long userId = user.getId();
    String jti = generateJtiToken(userId, jwtConfiguration.getAccessTokenExpirationInMinutes());
    Map<String, Object> accessClaims = generateClientAccessClaims(user, clientId, jti, facilityId, facilities, organisationId);
    String accessToken = generateToken(user.getId().toString(), accessClaims, user.getUsername(), new Date(accessTokenExpiration));
    Map<String, Object> refreshClaims = generateRefreshClaim(user, jti);
    String refreshToken = generateToken(user.getId().toString(), refreshClaims, user.getUsername(), new Date(refreshTokenExpiration));
    AuthenticationDto authenticationDto = new AuthenticationDto();
    authenticationDto.setAccessToken(accessToken);
    authenticationDto.setRefreshToken(refreshToken);
    return authenticationDto;
  }

  @Override
  public void cancelPasswordResetToken(User user) {
    invalidatePreviousPasswordResetTokens(user.getId());
  }

  private Map<String, Object> generateClientAccessClaims(User user, String clientId, String jti, String currentFacilityId, Set<Facility> facilities, Long organisationId) {
    Map<String, Object> accessClaims = new HashMap<>(15);
    accessClaims.put(KEY_ID, user.getId());
    accessClaims.put(KEY_EMPLOYEE_ID, user.getEmployeeId());
    accessClaims.put(KEY_FIRST_NAME, user.getFirstName());
    accessClaims.put(KEY_LAST_NAME, user.getLastName());
    accessClaims.put(KEY_EMAIL, user.getEmail());
    accessClaims.put(KEY_USERNAME, user.getUsername());
    accessClaims.put(KEY_SERVICES_ID, clientId);
    accessClaims.put(KEY_HAS_SET_CHALLENGE_QUESTION, user.isHasSetChallengeQuestion());
    accessClaims.put(KEY_ORGANISATIONS_ID, organisationId);
    accessClaims.put(KEY_FACILITIES, facilities.stream().map(f -> (new PartialFacilityDto()).setId(f.getIdAsString()).setName(f.getName())).collect(Collectors.toList()));
    accessClaims.put(KEY_FACILITY_IDS, facilities.stream().map(BaseEntity::getId).collect(Collectors.toSet()));
    accessClaims.put(KEY_CURRENT_FACILITY_ID, Long.valueOf(currentFacilityId));
    accessClaims.put(KEY_ROLES, user.getRoles().stream().map(r -> (new PartialRoleDto()).setId(r.getIdAsString()).setName(r.getName())).collect(Collectors.toList()));
    accessClaims.put(KEY_ROLE_NAMES, user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    accessClaims.put(KEY_JTI, jti);
    return accessClaims;
  }
}
