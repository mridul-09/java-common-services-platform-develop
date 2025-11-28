package com.leucine.services.jaas.service;

import com.leucine.services.jaas.dto.RoleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService {
  Page<RoleDto> getAll(String filters, Pageable pageable);
}
