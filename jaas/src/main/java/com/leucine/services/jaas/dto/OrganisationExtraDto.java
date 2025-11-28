package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class OrganisationExtraDto {
  private Long id;
  private JsonNode extras;
}
