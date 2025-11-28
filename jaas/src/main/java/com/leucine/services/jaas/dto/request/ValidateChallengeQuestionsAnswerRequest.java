package com.leucine.services.jaas.dto.request;

import lombok.Data;

@Data
public class ValidateChallengeQuestionsAnswerRequest {
  private Integer id;
  private String answer;
  private String token;
}
