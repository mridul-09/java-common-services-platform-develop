package com.leucine.services.jaas.controller.impl;

import com.leucine.services.jaas.controller.IRoleController;
import com.leucine.services.jaas.dto.RoleDto;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.service.IRoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleController implements IRoleController {

  private final IRoleService service;

  @Override
  public Response<Page<RoleDto>> getAll(String filters, Pageable pageable) {
    return Response.builder().data(service.getAll(filters, pageable)).build();
  }

}
