package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.constant.State;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class LicenseDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = -7944465480133134619L;
  long days;
  boolean expired;
  String facilityId;
  LocalDate graceEndsOn;
  LocalDate renewalDate;
  State.License state;
  String workflow;
}
