package com.leucine.services.jaas.dto.request;

import com.leucine.services.jaas.constant.Misc;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class EmailCheckRequest {

  @NotBlank(message = "Email cannot be blank")
  @Email(regexp = Misc.EMAIL_PATTERN, message = "Email must be a well-formed email")
  @Size(min = 6, message = "Email must be greater than 5 characters")
  @Size(max = 255, message = "Email must not exceed 255 characters")
  private String email;
}