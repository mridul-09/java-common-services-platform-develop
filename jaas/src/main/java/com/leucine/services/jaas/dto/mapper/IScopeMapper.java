package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.ScopeDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.model.Scope;
import org.mapstruct.Mapper;

@Mapper
public interface IScopeMapper extends IBaseMapper<ScopeDto, Scope> {
}
