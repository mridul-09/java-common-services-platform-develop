package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import com.leucine.services.jaas.model.Organisation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PasswordPolicyDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = -8437682673075270070L;
  private Integer minimumPasswordLength;
  private Integer minimumLowercaseCharacters;
  private Integer minimumUppercaseCharacters;
  private Integer minimumSpecialCharacters;
  private Integer minimumNumericCharacters;
  private boolean allowPasswordSimilarToUsernameOrEmail;
  private Integer maximumPasswordAge;
  private Integer minimumPasswordHistory;
  private Integer passwordExpiration;
  private Long organisationId;
}
