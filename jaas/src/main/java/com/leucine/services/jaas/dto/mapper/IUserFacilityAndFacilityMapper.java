package com.leucine.services.jaas.dto.mapper;

import com.leucine.services.jaas.dto.partial.PartialFacilityDto;
import com.leucine.services.jaas.model.Facility;
import com.leucine.services.jaas.model.UserFacilityMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = IFacilityMapper.class)
public interface IUserFacilityAndFacilityMapper {
  @Named("userFacilityMappingsToFacilities")
  static List<PartialFacilityDto> userFacilityMappingsToFacilities(List<UserFacilityMapping> userFacilityMappings) {
    return userFacilityMappings.stream()
      .sorted(Comparator.comparing(m -> m.getFacility().getName()))
      .map(m -> (new PartialFacilityDto()).setId(m.getFacility().getIdAsString()).setName(m.getFacility().getName())).collect(Collectors.toList());
  }

  @Named("userFacilityMappingsToFacilityIds")
  static Set<Long> userFacilityMappingsToFacilityIds(List<UserFacilityMapping> userFacilityMappings) {
    return userFacilityMappings.stream().map(m -> m.getFacility().getId()).collect(Collectors.toSet());
  }

  @Named("userFacilityMappingToFacility")
  static Facility userFacilityMappingToFacility(UserFacilityMapping userFacilityMappings) {
    return userFacilityMappings.getFacility();
  }
}
