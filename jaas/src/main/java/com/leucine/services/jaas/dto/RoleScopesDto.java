package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RoleScopesDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = -3067716076275316420L;
  Set<RoleDto> roles;
  Map<String, Map<String, Boolean>> scopes;
}
