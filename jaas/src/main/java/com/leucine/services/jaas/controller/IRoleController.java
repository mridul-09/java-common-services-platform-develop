package com.leucine.services.jaas.controller;

import com.leucine.services.jaas.dto.RoleDto;
import com.leucine.services.jaas.dto.response.Response;
import com.leucine.services.jaas.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/roles")
public interface IRoleController {
  @GetMapping
  @ResponseBody
  Response<Page<RoleDto>> getAll(@RequestParam(name = "filters", defaultValue = "", required = false) String filters,
                                 @SortDefault(sort = Role.DEFAULT_SORT) Pageable pageable);
}
