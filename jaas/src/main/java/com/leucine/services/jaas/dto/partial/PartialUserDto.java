package com.leucine.services.jaas.dto.partial;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PartialUserDto implements IBaseDto {
  private String id;
  private String employeeId;
  private String firstName;
  private String lastName;
  private String username;
  private String department;
  private String email;
  private String state;
  private boolean archived;
  private List<PartialRoleDto> roles = new ArrayList<>();
}
