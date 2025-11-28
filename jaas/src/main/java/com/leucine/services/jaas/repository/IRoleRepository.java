package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IRoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
  @Override
  Page<Role> findAll(Specification<Role> specification, Pageable pageable);

//  @Override
//  @EntityGraph(value = "role-scopeGroup-scope-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
//  List<Role> findAll();
}
