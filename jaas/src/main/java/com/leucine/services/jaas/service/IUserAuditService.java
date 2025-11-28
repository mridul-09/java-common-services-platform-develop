package com.leucine.services.jaas.service;

import com.leucine.services.jaas.dto.UserAuditDto;
import com.leucine.services.jaas.dto.UserDto;
import com.leucine.services.jaas.dto.partial.PartialUserForAudit;
import com.leucine.services.jaas.model.Role;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface IUserAuditService {

  Page<UserAuditDto> getAll(String filters, Pageable pageable);

  void register(PartialUserForAudit user);

  void login(PartialUserForAudit user);

  void logout(PrincipalUser principalUser);

  void loginFailed(PartialUserForAudit user);

  void loginFailedUserNotFound();

  void invalidSecretKey();

  void addUser(PartialUserForAudit user, String reason, PrincipalUser triggeredBy, List<Role> roles);

  void updateUser(UserDto oldDto, UserDto newDto, String reason, PartialUserForAudit user, PrincipalUser triggeredBy);

  void resetToken(PartialUserForAudit user, PrincipalUser triggeredBy);

  void cancelInvite(PartialUserForAudit user, PrincipalUser triggeredBy);

  void archive(PartialUserForAudit user, String reason, PrincipalUser triggeredBy);

  void unarchive(PartialUserForAudit user, String reason, PrincipalUser triggeredBy);

  void lock(PartialUserForAudit user);

  void unlock(PartialUserForAudit user, PrincipalUser triggeredBy);

  void updatePassword(PartialUserForAudit user);

  void updateChallengeQuestionsAnswer(PartialUserForAudit user);

  void challengeQuestionsAnswerValidationFailed(PartialUserForAudit user);

//  void resetPassword(User user, PrincipalUser triggeredBy);

  void inviteExpiredAsAdditionalVerificationFailedAttemptsExceeded(PartialUserForAudit user);

  void accountLockedAsChallengeQuestionFailedAttemptsExceeded(PartialUserForAudit user);

  void additionalVerificationFailed(PartialUserForAudit user);

  void passwordExpired(PartialUserForAudit user);

  void autoUnlocked(PartialUserForAudit user);

  void inviteExpired(PartialUserForAudit user);

  void administratorContactedForPasswordRecovery(PartialUserForAudit user);

  void administratorContactedForResetInvite(PartialUserForAudit user);

  void administratorContactedForUnlockAccount(PartialUserForAudit user);

  void switchFacility(PartialUserForAudit user, String facilityName);

  ResponseEntity<byte[]> downloadUserAudits(String filters) throws IOException;
}
