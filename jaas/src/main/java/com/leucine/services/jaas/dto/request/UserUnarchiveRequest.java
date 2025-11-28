package com.leucine.services.jaas.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserUnarchiveRequest {
  @Size(max = 255, message = "Reason must not exceed 255 characters")
  @NotBlank(message = "Reason cannot be empty")
  private String reason;
}
