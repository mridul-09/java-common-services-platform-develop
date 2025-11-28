package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import com.leucine.services.jaas.dto.partial.PartialFacilityDto;
import com.leucine.services.jaas.dto.partial.PartialOrganisationDto;
import com.leucine.services.jaas.dto.partial.PartialOrganisationSettingDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = -192685847691783947L;
  private String id;
  private String employeeId;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private boolean hasSetChallengeQuestion;
  private List<String> roles;
  private PartialOrganisationDto organisation;
  private List<PartialFacilityDto> facilities;
  private List<LicenseDto> licenses;
  private Set<Long> facilityIds;
  private Long currentFacilityId;
  private PartialOrganisationSettingDto settings;
  private String message;
  private HashMap<String, Boolean> permissions;
  private String tokenType;
  private String accessToken;
  private String refreshToken;
  private String token;
  private JsonNode extras;
  private String idToken;
  private String logoutUrl;
  private Map<Object, Object> features;

}
