package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.ChallengeQuestionDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.model.ChallengeQuestion;
import org.mapstruct.Mapper;

@Mapper
public interface IChallengeQuestionMapper extends IBaseMapper<ChallengeQuestionDto, ChallengeQuestion> {
}
