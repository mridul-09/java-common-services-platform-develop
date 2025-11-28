package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.RoleDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.dto.partial.PartialRoleDto;
import com.leucine.services.jaas.model.Role;
import com.leucine.services.jaas.model.Scope;
import com.leucine.services.jaas.model.ScopeGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {IScopeGroupMapper.class})
public interface IRoleMapper extends IBaseMapper<RoleDto, Role> {

  @Named("setToMapOfMap")
  public static Map<String, Map<String, Boolean>> map(Set<ScopeGroup> scopeGroups) {
    return scopeGroups.stream()
      .collect(Collectors.toMap(ScopeGroup::getName, sg -> sg.getScopes().stream().collect(Collectors.toMap(Scope::getName, s -> true, (x, y) -> y, LinkedHashMap::new)),
        (x, y) -> y, LinkedHashMap::new));
  }

  public static Set<String> convertRoleToRoleNames(Set<Role> roles) {
    return roles.stream().map(Role::getName).collect(Collectors.toSet());
  }

  PartialRoleDto toPartialDto(Role entity);

  List<PartialRoleDto> toPartialDto(List<Role> list);

  Set<PartialRoleDto> toPartialDto(Set<Role> list);

  @Override
  @Mapping(source = "scopeGroups", target = "scopes", qualifiedByName = "setToMapOfMap")
  RoleDto toDto(Role role);

}
