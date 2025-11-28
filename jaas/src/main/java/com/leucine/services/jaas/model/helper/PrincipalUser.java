package com.leucine.services.jaas.model.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.dto.OrganisationDto;
import com.leucine.services.jaas.dto.partial.PartialFacilityDto;
import com.leucine.services.jaas.dto.partial.PartialRoleDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrincipalUser implements Serializable {
  private static final long serialVersionUID = -8714903674405335802L;
  private Long id;
  private String idAsString;
  private String employeeId;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private boolean hasSetChallengeQuestion;
  private String token;
  private String serviceId;
  private Long organisationId;
  private OrganisationDto organisation;
  private List<PartialFacilityDto> facilities;
  private Set<Long> facilityIds;
  private Long currentFacilityId;
  private List<PartialRoleDto> roles;
  private Set<String> roleNames;
  private String state;
  private boolean archived;
  private Misc.UserType userType;

  @JsonIgnore
  private String jti;

  public String getIdAsString() {
    return String.valueOf(id);
  }

}
