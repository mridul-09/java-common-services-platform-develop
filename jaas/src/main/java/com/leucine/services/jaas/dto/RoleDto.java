package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RoleDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = -4921715026841985591L;
  String id;
  String name;
  //  Set<ScopeGroupDto> scopeGroups;
  Map<String, Map<String, Boolean>> scopes;

}
