package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.FacilityDto;
import com.leucine.services.jaas.dto.mapper.helper.IBaseMapper;
import com.leucine.services.jaas.dto.partial.PartialFacilityDto;
import com.leucine.services.jaas.model.Facility;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface IFacilityMapper extends IBaseMapper<FacilityDto, Facility> {
  PartialFacilityDto toPartialDto(Facility entity);

  List<PartialFacilityDto> toPartialDto(List<Facility> list);

  Set<PartialFacilityDto> toPartialDto(Set<Facility> list);

  List<Facility> toEntityFromPartialFacilityDto(List<PartialFacilityDto> list);
}
