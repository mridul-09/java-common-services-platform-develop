package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountLookupDto {
  private String username;
  private String type;
  private JsonNode extra;
  private String redirectUrl;
}
