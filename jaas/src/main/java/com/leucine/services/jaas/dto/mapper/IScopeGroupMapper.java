package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.ScopeGroupDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.model.Scope;
import com.leucine.services.jaas.model.ScopeGroup;
import org.mapstruct.Mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(uses = {IScopeMapper.class, IRoleMapper.class})
public interface IScopeGroupMapper extends IBaseMapper<ScopeGroupDto, ScopeGroup> {
  default Map<String, Map<String, Boolean>> toMap(List<ScopeGroup> list) {
    return list.stream()
             .collect(Collectors.toMap(ScopeGroup::getName,
               sg -> sg.getScopes().stream().collect(Collectors.toMap(Scope::getName, s -> true, (x, y) -> y, LinkedHashMap::new)),
               (x, y) -> y, LinkedHashMap::new));
  }
}
