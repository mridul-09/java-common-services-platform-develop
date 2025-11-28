package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import com.leucine.services.jaas.dto.partial.PartialOrganisationSettingDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class TokenDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = 6020303889984097680L;
  private String token;
  private String accessToken;
  private PartialOrganisationSettingDto settings;
//  private String refreshToken;

}
