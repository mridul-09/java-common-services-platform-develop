package com.leucine.services.jaas.task;

import com.leucine.services.jaas.constant.State;
import com.leucine.services.jaas.dto.mapper.IUserMapper;
import com.leucine.services.jaas.model.PasswordPolicy;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.repository.IPasswordPolicyRepository;
import com.leucine.services.jaas.repository.IUserRepository;
import com.leucine.services.jaas.service.IUserAuditService;
import com.leucine.services.jaas.util.DateTimeUtils;
import com.leucine.services.jaas.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoPasswordReset implements ITask, Serializable {
  private static final long serialVersionUID = 1224573444781298251L;

  private final transient IUserRepository userRepository;
  private final transient IPasswordPolicyRepository passwordPolicyRepository;
  private final transient IUserAuditService userAuditService;
  private final IUserMapper userMapper;

  @Scheduled(fixedRate = 300000)
  public void run() {
    log.info("[run] Auto password reset process started.");
    List<PasswordPolicy> passwordPolicies = passwordPolicyRepository.getAll();
    for (PasswordPolicy passwordPolicy : passwordPolicies) {
      log.info("[run] Password policy details, Organisation Id [{}], Password expiry (in days) [{}]", passwordPolicy.getOrganisationId(), passwordPolicy.getPasswordExpiration());
      if (!Utility.isNull(passwordPolicy.getPasswordExpiration())) {
        List<User> users = userRepository.getAllByExpiredPassword(passwordPolicy.getOrganisationId(), DateTimeUtils.getSecondsFromDays(passwordPolicy.getPasswordExpiration()),
          DateTimeUtils.now());
        for (User user : users) {
          log.info("[run] User's whose password reset is required, Username [{}], Password updated at [{}]", user.getUsername(), user.getPasswordUpdatedAt());
          userAuditService.passwordExpired(userMapper.toPartialUserForAudit(user));
          user.setState(State.User.PASSWORD_EXPIRED);
        }
        if (!users.isEmpty()) {
          userRepository.saveAll(users);
        }
      }
    }
    log.info("[run] Auto password reset process completed.");
  }
}
