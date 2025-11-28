package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.OrganisationDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.dto.partial.PartialOrganisationDto;
import com.leucine.services.jaas.model.Organisation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface IOrganisationMapper extends IBaseMapper<OrganisationDto, Organisation> {
  PartialOrganisationDto toPartialDto(Organisation entity);

  List<PartialOrganisationDto> toPartialDto(List<Organisation> list);
}
