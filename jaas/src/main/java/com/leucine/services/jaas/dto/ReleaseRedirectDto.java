package com.leucine.services.jaas.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class ReleaseRedirectDto implements Serializable {
  @Serial
  private static final long serialVersionUID = -8932679994415480934L;

  private String redirectUrl;
}
