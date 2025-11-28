package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.UserAuditDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.model.UserAudit;
import org.mapstruct.Mapper;

@Mapper
public interface IUserAuditMapper extends IBaseMapper<UserAuditDto, UserAudit> {
}
