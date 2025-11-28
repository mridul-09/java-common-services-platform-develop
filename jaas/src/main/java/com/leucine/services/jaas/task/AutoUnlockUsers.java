package com.leucine.services.jaas.task;

import com.leucine.services.jaas.constant.State;
import com.leucine.services.jaas.dto.mapper.IUserMapper;
import com.leucine.services.jaas.model.OrganisationSetting;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.repository.IOrganisationSettingRepository;
import com.leucine.services.jaas.repository.IUserRepository;
import com.leucine.services.jaas.service.IUserAuditService;
import com.leucine.services.jaas.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoUnlockUsers implements ITask, Serializable {
  private static final long serialVersionUID = -5589507461489681356L;

  private final transient IUserRepository userRepository;
  private final transient IOrganisationSettingRepository organisationSettingRepository;
  private final transient IUserAuditService userAuditService;
  private final IUserMapper userMapper;

  @Scheduled(fixedRate = 300000)
  public void run() {
    log.info("[run] Auto unlock users process started.");
    List<OrganisationSetting> organisationSettings = organisationSettingRepository.getAll();
    for (OrganisationSetting organisationSetting : organisationSettings) {
      log.info("[run] Organisation setting details, Organisation Id [{}], Auto unlock after (in minutes) [{}]", organisationSetting.getOrganisationId(), organisationSetting.getAutoUnlockAfter());
      List<User> lockedUsers = userRepository.getAllByLocked(organisationSetting.getOrganisationId(),
        DateTimeUtils.getSecondsFromMinutes(organisationSetting.getAutoUnlockAfter()),
        DateTimeUtils.now());
      List<User> unlockUsers = new ArrayList<>();
      for (User lockedUser : lockedUsers) {
        log.info("[run] User that are set for auto unlock process, Username [{}], Locked at [{}]", lockedUser.getUsername(), lockedUser.getLockedAt());
        userAuditService.autoUnlocked(userMapper.toPartialUserForAudit(lockedUser));
        var currentState = lockedUser.getState();
        if (currentState == State.User.UNREGISTERED_LOCKED) {
          lockedUser.setState(State.User.UNREGISTERED).setLockedAt(null).setFailedLoginAttempts(0);
        } else if (currentState == State.User.REGISTERED_LOCKED) {
          lockedUser.setState(State.User.REGISTERED).setLockedAt(null).setFailedLoginAttempts(0);
        }
        unlockUsers.add(lockedUser);
      }
      if (!unlockUsers.isEmpty()) {
        userRepository.saveAll(unlockUsers);
      }
    }
    log.info("[run] Auto unlock users process completed.");
  }
}
