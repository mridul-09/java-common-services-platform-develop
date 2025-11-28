package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import com.leucine.services.jaas.dto.partial.PartialFacilityDto;
import com.leucine.services.jaas.dto.partial.PartialRoleDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(value = JsonInclude.Include.ALWAYS)
public class UserDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = -2649244085447835915L;
  private String id;
  private String employeeId;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String department;
  private OrganisationDto organisation;
  private List<PartialFacilityDto> facilities;
  private List<PartialRoleDto> roles;
  private String state;
  private boolean archived;
  private Misc.UserType userType;
}
