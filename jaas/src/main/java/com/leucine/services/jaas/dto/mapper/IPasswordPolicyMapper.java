package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.PasswordPolicyDto;
import com.leucine.services.jaas.dto.PermissionDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.dto.partial.PartialPasswordPolicyDto;
import com.leucine.services.jaas.dto.partial.PartialPermissionDto;
import com.leucine.services.jaas.model.PasswordPolicy;
import com.leucine.services.jaas.model.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface IPasswordPolicyMapper extends IBaseMapper<PasswordPolicyDto, PasswordPolicy> {
  PartialPasswordPolicyDto toPartialDto(PasswordPolicy entity);

  List<PartialPasswordPolicyDto> toPartialDto(List<PasswordPolicy> list);
}
