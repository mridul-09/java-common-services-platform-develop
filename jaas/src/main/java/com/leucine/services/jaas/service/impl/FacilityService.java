package com.leucine.services.jaas.service.impl;

import com.leucine.services.jaas.constant.Message;
import com.leucine.services.jaas.dto.FacilityDto;
import com.leucine.services.jaas.dto.mapper.IFacilityMapper;
import com.leucine.services.jaas.exception.ResourceNotFoundException;
import com.leucine.services.jaas.model.Facility;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import com.leucine.services.jaas.model.helper.SpecificationBuilder;
import com.leucine.services.jaas.model.helper.search.SearchCriteria;
import com.leucine.services.jaas.model.helper.search.SearchOperator;
import com.leucine.services.jaas.repository.IFacilityRepository;
import com.leucine.services.jaas.service.IFacilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacilityService implements IFacilityService {
  private final IFacilityRepository repository;
  private final IFacilityMapper mapper;

  @Override
  public Page<FacilityDto> getAll(String filters, Pageable pageable) {
    PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    SearchCriteria organisationSearchCriteria =
      (new SearchCriteria()).setField(Facility.ORGANISATION_ID).setOp(SearchOperator.EQ.toString()).setValues(Collections.singletonList(principalUser.getOrganisationId()));
    Specification<Facility> specification = SpecificationBuilder.createSpecification(filters, Collections.singletonList(organisationSearchCriteria));
    Page<Facility> facilities = repository.findAll(specification, pageable);
    return new PageImpl<>(mapper.toDto(facilities.getContent()), pageable, facilities.getTotalElements());
  }

  @Override
  public FacilityDto getFacilityById(Long id) {
    log.info("[getFacilityById] Fetching Facility for Id: {}", id);
    Facility facility = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Message.Error.RESOURCE_NOT_FOUND));
    return mapper.toDto(facility);
  }
}
