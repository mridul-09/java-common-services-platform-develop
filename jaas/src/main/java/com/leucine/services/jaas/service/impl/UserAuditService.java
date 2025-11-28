package com.leucine.services.jaas.service.impl;

import com.leucine.services.jaas.constant.Action;
import com.leucine.services.jaas.constant.Message;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.constant.Severity;
import com.leucine.services.jaas.dto.AuditDateGroupDto;
import com.leucine.services.jaas.dto.AuditEntryDto;
import com.leucine.services.jaas.dto.AuditReportDataDto;
import com.leucine.services.jaas.dto.UserAuditDto;
import com.leucine.services.jaas.dto.UserChangeDto;
import com.leucine.services.jaas.dto.UserDto;
import com.leucine.services.jaas.dto.mapper.IUserAuditMapper;
import com.leucine.services.jaas.dto.partial.PartialUserForAudit;
import com.leucine.services.jaas.model.Facility;
import com.leucine.services.jaas.model.Role;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.model.UserAudit;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import com.leucine.services.jaas.model.helper.SpecificationBuilder;
import com.leucine.services.jaas.model.helper.search.SearchCriteria;
import com.leucine.services.jaas.model.helper.search.SearchOperator;
import com.leucine.services.jaas.repository.IFacilityRepository;
import com.leucine.services.jaas.repository.IUserAuditRepository;
import com.leucine.services.jaas.repository.IUserRepository;
import com.leucine.services.jaas.service.IUserAuditService;
import com.leucine.services.jaas.util.DateTimeUtils;
import com.leucine.services.jaas.util.PdfGeneratorUtil;
import com.leucine.services.jaas.util.UserDtoChangeFinder;
import com.leucine.services.jaas.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.leucine.services.jaas.util.Utility.snakeToTitleCase;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuditService implements IUserAuditService {

  private final IUserAuditRepository repository;
  private final IUserAuditMapper mapper;
  private final IUserRepository userRepository;
  private final PdfGeneratorUtil pdfGeneratorUtil;
  private final IFacilityRepository facilityRepository;

  @Override
  public Page<UserAuditDto> getAll(String filters, Pageable pageable) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    SearchCriteria facilitySearchCriteria = null;
    Long currentFacilityId = principalUser.getCurrentFacilityId();
    if (currentFacilityId != null && !currentFacilityId.equals(Misc.ALL_FACILITY_ID)) {
      facilitySearchCriteria =
        (new SearchCriteria()).setField(UserAudit.FACILITY_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(currentFacilityId));
    }
    SearchCriteria organisationSearchCriteria =
      (new SearchCriteria()).setField(User.ORGANISATION_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(principalUser.getOrganisationId()));
    Specification<UserAudit> specification = SpecificationBuilder.createSpecification(filters, Arrays.asList(organisationSearchCriteria, facilitySearchCriteria));
//    Specification<UserAudit> specification = Utility.createSpecification(filters);
    Page<UserAudit> audits = repository.findAll(specification, pageable);
    return new PageImpl<>(mapper.toDto(audits.getContent()), pageable, audits.getTotalElements());
  }

  @Override
  @Async("auditExecutor")
  public void register(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.REGISTER)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.REGISTER, user.getFirstName(), user.getLastName(), user.getEmployeeId(), snakeToTitleCase(userRepository.findRoleNameByEmployeeId(user.getEmployeeId()))))
    );
  }

  private void audit(UserAudit userAudit) {
    try {
//      Thread.sleep(1000L);
      repository.save(userAudit);
    } catch (Exception e) {
      log.error("Error saving audit logs. userAudit [{}]", userAudit);
    }
  }

  @Override
  @Async("auditExecutor")
  public void login(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.LOGIN)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.LOGIN_SUCCESS, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void logout(PrincipalUser principalUser) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.LOGOUT)
      .setOrganisationId(principalUser.getOrganisationId())
      .setFacilityIds(principalUser.getFacilityIds().stream().map(Object::toString).toArray(String[]::new))
      .setTriggeredBy(principalUser.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.LOGOUT, principalUser.getFirstName(), principalUser.getLastName(), principalUser.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void loginFailed(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.CRITICAL)
      .setAction(Action.UserAudit.LOGIN)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.LOGIN_FAILED, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void loginFailedUserNotFound() {
    audit((new UserAudit())
      .setSeverity(Severity.CRITICAL)
      .setAction(Action.UserAudit.LOGIN)
      .setTriggeredBy(User.SYSTEM_USER_ID)
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(Message.UserAudit.LOGIN_FAILED_USERNAME_NOT_FOUND)
    );
  }

  @Override
  @Async("auditExecutor")
  public void invalidSecretKey() {
    audit((new UserAudit())
      .setSeverity(Severity.CRITICAL)
      .setAction(Action.UserAudit.INVALID_TOKEN)
      .setTriggeredBy(User.SYSTEM_USER_ID)
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(Message.UserAudit.INVALID_SECRET_KEY)
    );
  }

  @Override
  @Async("auditExecutor")
  public void addUser(PartialUserForAudit user, String reason, PrincipalUser triggeredBy, List<Role> roles) {
    for (Role role : roles) {
      audit((new UserAudit())
        .setSeverity(Severity.INFORMATION)
        .setAction(Action.UserAudit.INVITE_USER)
        .setOrganisationId(triggeredBy.getOrganisationId())
        .setFacilityIds(user.getFacilityIds())
        .setTriggeredBy(triggeredBy.getId())
        .setTriggeredAt(DateTimeUtils.now())
        .setDetails(MessageFormat.format(Message.UserAudit.ADD_USER, triggeredBy.getFirstName(), triggeredBy.getLastName(), triggeredBy.getEmployeeId(), user.getFirstName(),
          user.getLastName(), user.getEmployeeId(), snakeToTitleCase(role.getName()), reason))
      );
    }
  }

  @Override
  @Async("auditExecutor")
  public void updateUser(UserDto oldDto, UserDto newDto, String reason, PartialUserForAudit user, PrincipalUser triggeredBy) {
    List<UserChangeDto> changes = UserDtoChangeFinder.findChanges(oldDto, newDto);
    for (UserChangeDto change : changes) {
      audit((new UserAudit())
        .setSeverity(Severity.INFORMATION)
        .setAction(Action.UserAudit.UPDATE_USER)
        .setOrganisationId(triggeredBy.getOrganisationId())
        .setFacilityIds(user.getFacilityIds())
        .setTriggeredBy(triggeredBy.getId())
        .setTriggeredAt(DateTimeUtils.now())
        .setDetails(MessageFormat.format(Message.UserAudit.UPDATE_USER, triggeredBy.getFirstName(), triggeredBy.getLastName(),
          triggeredBy.getEmployeeId(),
          user.getFirstName(), user.getLastName(), user.getEmployeeId(), change.getField(), change.getOldValue(), change.getNewValue(), reason)));
    }
  }

  @Override
  @Async("auditExecutor")
  public void resetToken(PartialUserForAudit user, PrincipalUser triggeredBy) {
    String message = triggeredBy != null ? MessageFormat.format(Message.UserAudit.RESET_TOKEN, triggeredBy.getFirstName(), triggeredBy.getLastName(), triggeredBy.getEmployeeId(),
      user.getFirstName(), user.getLastName(), user.getEmployeeId()) : MessageFormat.format(Message.UserAudit.PASSWORD_RECOVERY_EMAIL, user.getFirstName(), user.getLastName(), user.getEmployeeId());
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.RESET_TOKEN)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(message)
    );
  }

  @Override
  @Async("auditExecutor")
  public void cancelInvite(PartialUserForAudit user, PrincipalUser triggeredBy) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.CANCEL_INVITE)
      .setOrganisationId(triggeredBy.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.CANCEL_INVITE, triggeredBy.getFirstName(), triggeredBy.getLastName(), triggeredBy.getEmployeeId(),
        user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void archive(PartialUserForAudit user, String reason, PrincipalUser triggeredBy) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.ARCHIVE_USER)
      .setOrganisationId(triggeredBy.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.ARCHIVE_USER, triggeredBy.getFirstName(), triggeredBy.getLastName(), triggeredBy.getEmployeeId(),
        user.getFirstName(), user.getLastName(), user.getEmployeeId(), reason))
    );
  }

  @Override
  @Async("auditExecutor")
  public void unarchive(PartialUserForAudit user, String reason, PrincipalUser triggeredBy) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.UNARCHIVE_USER)
      .setOrganisationId(triggeredBy.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.UNARCHIVE_USER, triggeredBy.getFirstName(), triggeredBy.getLastName(), triggeredBy.getEmployeeId(),
        user.getFirstName(), user.getLastName(), user.getEmployeeId(), reason))
    );
  }

  @Override
  @Async("auditExecutor")
  public void lock(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.LOCK_USER)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.LOCK_USER, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void unlock(PartialUserForAudit user, PrincipalUser triggeredBy) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.UNLOCK_USER)
      .setOrganisationId(triggeredBy.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.UNLOCK_USER, triggeredBy.getFirstName(), triggeredBy.getLastName(), triggeredBy.getEmployeeId(),
        user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void updatePassword(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.UPDATE_PASSWORD)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.UPDATE_PASSWORD, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void updateChallengeQuestionsAnswer(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.UPDATE_CHALLENGE_QUESTIONS)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.UPDATE_CHALLENGE_QUESTIONS_ANSWER, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void challengeQuestionsAnswerValidationFailed(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.CRITICAL)
      .setAction(Action.UserAudit.CHALLENGE_QUESTIONS_ANSWER_VALIDATION_FAILED)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.CHALLENGE_QUESTIONS_ANSWER_VALIDATION_FAILED, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void additionalVerificationFailed(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.ADDITIONAL_VERIFICATION_FAILED)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.ADDITIONAL_VERIFICATION_FAILED, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void inviteExpiredAsAdditionalVerificationFailedAttemptsExceeded(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.CRITICAL)
      .setAction(Action.UserAudit.INVITE_EXPIRED)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.INVITE_EXPIRED_AS_ADDITIONAL_VERIFICATION_FAILED_ATTEMPTS_EXCEEDED, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void accountLockedAsChallengeQuestionFailedAttemptsExceeded(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.CRITICAL)
      .setAction(Action.UserAudit.LOCK_USER)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.ACCOUNT_LOCKED_AS_CHALLENGE_QUESTION_FAILED_ATTEMPTS_EXCEEDED, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void passwordExpired(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.PASSWORD_EXPIRED)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.SYSTEM_PASSWORD_EXPIRED, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void autoUnlocked(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.UNLOCK_USER)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.SYSTEM_AUTO_UNLOCK, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void inviteExpired(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.INVITE_EXPIRED)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.SYSTEM_INVITE_EXPIRED, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void administratorContactedForPasswordRecovery(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.NOTIFY_ADMIN)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.SYSTEM_ADMIN_NOTIFY_FOR_PASSWORD_RESET, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void administratorContactedForResetInvite(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.NOTIFY_ADMIN)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.SYSTEM_ADMIN_NOTIFY_FOR_RESET_INVITE, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void administratorContactedForUnlockAccount(PartialUserForAudit user) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.NOTIFY_ADMIN)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.SYSTEM_ADMIN_NOTIFY_FOR_UNLOCK_ACCOUNT, user.getFirstName(), user.getLastName(), user.getEmployeeId()))
    );
  }

  @Override
  @Async("auditExecutor")
  public void switchFacility(PartialUserForAudit user, String facilityName) {
    audit((new UserAudit())
      .setSeverity(Severity.INFORMATION)
      .setAction(Action.UserAudit.FACILITY_SWITCH)
      .setOrganisationId(user.getOrganisationId())
      .setFacilityIds(user.getFacilityIds())
      .setTriggeredBy(user.getId())
      .setTriggeredAt(DateTimeUtils.now())
      .setDetails(MessageFormat.format(Message.UserAudit.FACILITY_SWITCH, user.getFirstName(), user.getLastName(), user.getEmployeeId(), facilityName))
    );
  }


  @Override
  public ResponseEntity<byte[]> downloadUserAudits(String filters) throws IOException {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Specification<UserAudit> spec = SpecificationBuilder.createSpecification(filters);
    Pageable unpaged = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, UserAudit.DEFAULT_SORT));
    List<UserAudit> allAudits = repository.findAll(spec, unpaged).getContent();
    boolean hasFacilityContext = principalUser.getCurrentFacilityId() != null && !principalUser.getCurrentFacilityId().equals(Misc.ALL_FACILITY_ID);
    String facilityName = Misc.ALL_FACILITY_NAME;
    String dateFormat = null;
    String timeFormat = null;
    String timeZone = null;
    if (hasFacilityContext) {
      Facility facility = facilityRepository.findById(principalUser.getCurrentFacilityId())
        .orElseThrow(() -> new IllegalStateException("Facility not found for ID: " + principalUser.getCurrentFacilityId()));
      facilityName = facility.getName();

      dateFormat = facility.getDateFormat();
      timeFormat = facility.getTimeFormat();
      timeZone = facility.getTimeZone();
    }
    else {
      Facility facility = facilityRepository.findById(-1L).orElseThrow(() -> new IllegalStateException("Facility not found for ID: -1"));
      facilityName = facility.getName();

      dateFormat = facility.getDateFormat();
      timeFormat = facility.getTimeFormat();
      timeZone = facility.getTimeZone();
    }
    // Group audits by date using POJO structure
    Map<String, List<AuditEntryDto>> groupedAuditData = new LinkedHashMap<>();
    for (UserAudit audit : allAudits) {
      Long triggeredAt = audit.getTriggeredAt();
      ZonedDateTime dateTime = Instant.ofEpochSecond(triggeredAt).atZone(ZoneId.of(timeZone));
      String dateKey = dateTime.format(DateTimeFormatter.ofPattern(dateFormat));
      String timeValue = dateTime.format(DateTimeFormatter.ofPattern(timeFormat));

      AuditEntryDto auditEntry = AuditEntryDto.builder()
        .timestamp(timeValue)
        .details(audit.getDetails())
        .severity(audit.getSeverity())
        .build();

      groupedAuditData.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(auditEntry);
    }

    // Create AuditDateGroupDto objects with pre-calculated counts (no sorting, maintain database order)
    List<AuditDateGroupDto> auditDateGroups = new ArrayList<>();
    for (Map.Entry<String, List<AuditEntryDto>> entry : groupedAuditData.entrySet()) {
      String date = entry.getKey();
      List<AuditEntryDto> auditEntries = entry.getValue();
      
      long criticalCount = auditEntries.stream()
          .filter(auditEntry -> Severity.CRITICAL.equals(auditEntry.getSeverity()))
          .count();
      
      auditDateGroups.add(AuditDateGroupDto.builder()
          .date(date)
          .auditEntries(auditEntries)
          .criticalCount(criticalCount)
          .totalCount(auditEntries.size())
          .build());
    }

    AuditReportDataDto auditReportData = AuditReportDataDto.builder()
      .auditDateGroups(auditDateGroups)
      .build();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "user-audit-report.pdf");

    byte[] pdfBytes = pdfGeneratorUtil.generate(
      principalUser.getFirstName() + " " + principalUser.getLastName(), 
      principalUser.getEmployeeId(), 
      facilityName, 
      principalUser.getOrganisationId(),
      auditReportData
    );

    return ResponseEntity.ok().headers(headers).body(pdfBytes);
  }

}
