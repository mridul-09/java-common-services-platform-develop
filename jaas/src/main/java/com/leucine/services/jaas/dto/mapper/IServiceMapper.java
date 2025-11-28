package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.ServicesDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.model.Services;
import org.mapstruct.Mapper;

@Mapper
public interface IServiceMapper extends IBaseMapper<ServicesDto, Services> {
}
