package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SecuredUserDto extends UserDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = -2453481265067443989L;
  private ChallengeQuestionDto challengeQuestion;
}
