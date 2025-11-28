package com.leucine.services.jaas.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UsernameCheckRequest {

  @NotBlank(message = "Username cannot be blank")
  @Size(min = 3, message = "Username must be greater than 3 characters")
  @Size(max = 45, message = "Username cannot exceed 45 characters")
  @Pattern(regexp = "[a-z0-9][a-z0-9._]+", message = "Username should start with a-z or 0-9 and can only contain underscore(_), and period(.) as special characters")
  private String username;
}