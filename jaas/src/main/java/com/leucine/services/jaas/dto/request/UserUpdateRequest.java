package com.leucine.services.jaas.dto.request;

import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.dto.FacilityDto;
import com.leucine.services.jaas.dto.RoleDto;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserUpdateRequest {
  private Long id;

  @Size(max = 255, message = "First name must not exceed 255 characters")
  @NotBlank(message = "First name cannot be empty")
  private String firstName;

  @Size(max = 255, message = "Last name must not exceed 255 characters")
  private String lastName;

  @Size(max = 255, message = "Department must not exceed 255 characters")
  private String department;

  @Size(max = 255, message = "Email must not exceed 255 characters")
//  @Size(min = 6, message = "Email must be a well-formed email")
  @Email(regexp = Misc.EMAIL_PATTERN, message = "Email must be a well-formed email")
  private String email;

  @NotEmpty(message = "User must belong to at-least one facility.")
  private List<FacilityDto> facilities;

  @NotEmpty(message = "User must be mapped to at-least one role")
  private List<RoleDto> roles;

  @Size(max = 255, message = "Reason must not exceed 255 characters")
  @NotBlank(message = "Reason cannot be empty")
  private String reason;
}
