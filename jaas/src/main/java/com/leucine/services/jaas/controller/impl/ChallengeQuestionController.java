package com.leucine.services.jaas.controller.impl;

import com.leucine.services.jaas.controller.IChallengeQuestionController;
import com.leucine.services.jaas.dto.ChallengeQuestionDto;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.service.IChallengeQuestionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ChallengeQuestionController implements IChallengeQuestionController {

  private final IChallengeQuestionService service;

  @Override
  public Response<List<ChallengeQuestionDto>> getAll(boolean archived) {
    return Response.builder().data(service.getAll(archived)).build();
  }

}
