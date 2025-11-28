package com.leucine.services.jaas.task;

import com.leucine.services.jaas.model.Token;
import com.leucine.services.jaas.repository.ITokenRepository;
import com.leucine.services.jaas.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoDeleteExpiredTokens implements ITask, Serializable {
  private static final long serialVersionUID = 1224573444781298251L;

  private final transient ITokenRepository tokenRepository;

  @Scheduled(fixedRate = 1800000)
//  @Scheduled(fixedRate = 60000)
  public void run() {
    //  NOTE: Do not expire password reset token
    var deleteAfterExpiryAfterInHours = 48L;
    log.info("[run] Auto delete expired tokens after {} hours of expiration, process started.", deleteAfterExpiryAfterInHours);
    List<Token> tokens = tokenRepository.getAllExpiredTokenAfterOffset(deleteAfterExpiryAfterInHours * 60L * 60L, DateTimeUtils.now());
    for (Token token : tokens) {
      log.info("[run] Tokens to be deleted, Details: User Id [{}], Token type [{}], Expiration [{}]", token.getUserId(), token.getType(), token.getExpiration());
    }
    tokenRepository.deleteAll(tokens);
    log.info("[run] Auto delete expired tokens process completed.");
  }
}
