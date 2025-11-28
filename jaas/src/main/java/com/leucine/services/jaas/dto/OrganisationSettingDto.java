package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisationSettingDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = -4113194062851958811L;
  private String logoUrl;
  private Integer accessTokenExpirationInMinutes;
  private Integer refreshTokenExpirationInMinutes;
  private Integer sessionIdleTimeoutInMinutes;
}
