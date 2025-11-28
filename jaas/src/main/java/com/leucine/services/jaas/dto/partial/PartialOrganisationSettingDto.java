package com.leucine.services.jaas.dto.partial;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartialOrganisationSettingDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = 7954779059053426576L;
  private String logoUrl;
  private Integer accessTokenExpirationInMinutes;
  private Integer refreshTokenExpirationInMinutes;
  private Integer sessionIdleTimeoutInMinutes;
  private Misc.UserType ssoType;
  private PartialPasswordPolicyDto passwordPolicy;
}
