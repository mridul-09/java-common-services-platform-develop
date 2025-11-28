package com.leucine.services.jaas.service;

import com.leucine.services.jaas.dto.FacilityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFacilityService {
  Page<FacilityDto> getAll(String filters, Pageable pageable);

  FacilityDto getFacilityById(Long id);
}
