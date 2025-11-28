package com.leucine.services.jaas.dto.request;

import com.leucine.services.jaas.constant.Misc;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserBasicInformationUpdateRequest {
  private Long id;

  @Size(max = 255, message = "First name must not exceed 255 characters")
  @NotBlank(message = "First name cannot be empty")
  private String firstName;

  @Size(max = 255, message = "Last name must not exceed 255 characters")
  private String lastName;

  @Size(max = 255, message = "Email must not exceed 255 characters")
//  @Size(min = 6, message = "Email must be a well-formed email")
  @Email(regexp = Misc.EMAIL_PATTERN, message = "Email must be a well-formed email")
  private String email;

  @Size(max = 255, message = "Reason must not exceed 255 characters")
  @NotBlank(message = "Reason cannot be empty")
  private String reason;
}
