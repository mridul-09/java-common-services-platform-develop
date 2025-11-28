package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.Role;
import com.leucine.services.jaas.model.RolePermissionMapping;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRolePermissionRepository extends JpaRepository<RolePermissionMapping, Long> {

  @EntityGraph(value = "role-permission-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
  List<RolePermissionMapping> findAllByRoleIn(List<Role> roles);

}
