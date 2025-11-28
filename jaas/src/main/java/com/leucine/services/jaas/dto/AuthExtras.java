package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthExtras {
  private JsonNode extras;
  private String message;
}
