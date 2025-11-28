package com.leucine.services.jaas.dto.partial;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartialUserForAudit implements Serializable {
  private static final long serialVersionUID = -8714903674405335802L;
  private Long id;
  private String employeeId;
  private String firstName;
  private String lastName;
  private String email;
  private Long organisationId;
  private String[] facilityIds;
  private boolean archived;
}