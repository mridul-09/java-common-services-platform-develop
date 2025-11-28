package com.leucine.services.jaas.service.impl;

import com.leucine.services.jaas.dto.ChallengeQuestionDto;
import com.leucine.services.jaas.dto.mapper.IChallengeQuestionMapper;
import com.leucine.services.jaas.repository.IChallengeQuestionRepository;
import com.leucine.services.jaas.service.IChallengeQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeQuestionService implements IChallengeQuestionService {
  private final IChallengeQuestionRepository repository;
  private final IChallengeQuestionMapper mapper;

  @Override
  public List<ChallengeQuestionDto> getAll(boolean archived) {
    // TODO add ARCHIVED filter
//    SearchCriteria archivedSearchCriteria = (new SearchCriteria()).setField(ChallengeQuestion.ARCHIVED).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(false));
//    Specification<User> specification = SpecificationBuilder.createSpecification(archivedSearchCriteria);
    return mapper.toDto(repository.findAll());
  }
}
