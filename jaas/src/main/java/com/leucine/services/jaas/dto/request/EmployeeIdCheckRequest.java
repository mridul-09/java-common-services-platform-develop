package com.leucine.services.jaas.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class EmployeeIdCheckRequest {

  @NotBlank(message = "Employee ID cannot be blank")
  @Size(min = 4, message = "Employee ID must be greater than 3 characters")
  @Size(max = 255, message = "Employee ID must not exceed 255 characters")
  private String employeeId;
}