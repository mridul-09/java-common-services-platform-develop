package com.leucine.services.jaas.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SSORedirectDto {
  private Map<Object, Object> data;
}
