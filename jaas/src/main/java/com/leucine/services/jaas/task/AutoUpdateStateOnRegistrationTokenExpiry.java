package com.leucine.services.jaas.task;

import com.leucine.services.jaas.constant.State;
import com.leucine.services.jaas.dto.mapper.IUserMapper;
import com.leucine.services.jaas.model.Token;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.repository.ITokenRepository;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoUpdateStateOnRegistrationTokenExpiry implements ITask, Serializable {
  private static final long serialVersionUID = 1224573444781298251L;

  private final transient IUserRepository userRepository;
  private final transient ITokenRepository tokenRepository;
  private final transient IUserAuditService userAuditService;
  private final IUserMapper userMapper;

  //  @Scheduled(fixedRate = 60000)
  @Scheduled(fixedRate = 300000)
  public void run() {
    log.info("[run] Auto update user state on invite expiry process started.");
    List<Token> tokens = tokenRepository.getAllExpiredRegistrationToken(DateTimeUtils.now());
    List<Long> userIds = tokens.stream().map(Token::getUserId).distinct().collect(Collectors.toList());
    if (!Utility.isEmpty(userIds)) {
      log.info("[run] User's whose registration invite has expired user ids [{}]", userIds);
      userRepository.updateState(State.User.INVITE_EXPIRED, userIds);

      List<User> users = userRepository.findAllById(userIds);
      for (User user : users) {
        if (!user.getState().equals(State.User.INVITE_EXPIRED)) {
          userAuditService.inviteExpired(userMapper.toPartialUserForAudit(user));
        }
      }
    }
    log.info("[run] Auto update user state on invite expiry process completed.");
  }
}
