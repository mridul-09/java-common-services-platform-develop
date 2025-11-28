package com.leucine.services.jaas.controller;

import com.leucine.services.jaas.dto.FacilityDto;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.model.helper.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/facilities")
public interface IFacilityController {
  @GetMapping
  @ResponseBody
  Response<Page<FacilityDto>> getAll(@RequestParam(name = "filters", defaultValue = "", required = false) String filters,
                                     @SortDefault(sort = BaseEntity.DEFAULT_SORT) Pageable pageable);

  @GetMapping("/{facilityId}")
  Response<FacilityDto> getFacilityById( @PathVariable Long facilityId);
}
