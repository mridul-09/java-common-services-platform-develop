package com.leucine.services.jaas.dto.request;

import com.leucine.services.jaas.constant.Misc;
import lombok.Data;

@Data
public class ValidateCredentialsRequest {
  private String password;
  private Misc.ValidationPurpose purpose;
  private String code;
  private String state;
}
