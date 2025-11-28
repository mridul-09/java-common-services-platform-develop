package com.leucine.services.jaas.service;

import com.leucine.services.jaas.dto.ChallengeQuestionDto;

import java.util.List;

public interface IChallengeQuestionService {
  List<ChallengeQuestionDto> getAll(boolean archived);
}
