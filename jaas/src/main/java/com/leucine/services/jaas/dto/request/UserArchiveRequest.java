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
public class UserArchiveRequest {
  @Size(max = 255, message = "Reason must not exceed 255 characters")
  @NotBlank(message = "Reason cannot be empty")
  private String reason;
}
