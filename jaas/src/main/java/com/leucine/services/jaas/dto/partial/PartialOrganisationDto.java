package com.leucine.services.jaas.dto.partial;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartialOrganisationDto implements IBaseDto {
  private String id;
  private String name;
}
