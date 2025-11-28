package com.leucine.services.jaas.service.impl;

import com.leucine.services.jaas.dto.RoleDto;
import com.leucine.services.jaas.dto.mapper.IRoleMapper;
import com.leucine.services.jaas.model.Role;
import com.leucine.services.jaas.model.helper.SpecificationBuilder;
import com.leucine.services.jaas.repository.IRoleRepository;
import com.leucine.services.jaas.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

  private final IRoleRepository repository;
  private final IRoleMapper mapper;

  @Override
  public Page<RoleDto> getAll(String filters, Pageable pageable) {
    Specification<Role> specification = SpecificationBuilder.createSpecification(filters);
    Page<Role> roles = repository.findAll(specification, pageable);
    return new PageImpl<>(mapper.toDto(roles.getContent()), pageable, roles.getTotalElements());
  }

}
