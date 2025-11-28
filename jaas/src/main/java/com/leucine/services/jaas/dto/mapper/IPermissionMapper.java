package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.PermissionDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.dto.partial.PartialPermissionDto;
import com.leucine.services.jaas.model.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface IPermissionMapper extends IBaseMapper<PermissionDto, Permission> {
  PartialPermissionDto toPartialDto(Permission entity);

  List<PartialPermissionDto> toPartialDto(List<Permission> list);
}
