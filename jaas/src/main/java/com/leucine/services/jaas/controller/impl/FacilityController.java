package com.leucine.services.jaas.controller.impl;

import com.leucine.services.jaas.controller.IFacilityController;
import com.leucine.services.jaas.dto.FacilityDto;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.service.IFacilityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class FacilityController implements IFacilityController {

  private final IFacilityService service;

  @Override
  public Response<Page<FacilityDto>> getAll(String filters, Pageable pageable) {
    return Response.builder().data(service.getAll(filters, pageable)).build();
  }

  @Override
  public Response<FacilityDto> getFacilityById(Long facilityId) {
    return Response.builder().data(service.getFacilityById(facilityId)).build();
  }

}
