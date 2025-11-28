package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.ScopeGroup;
import com.leucine.services.jaas.model.Services;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IScopeGroupRepository extends JpaRepository<ScopeGroup, String>, JpaSpecificationExecutor<ScopeGroup> {

  @EntityGraph(value = "scopeGroup-scope-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
  List<ScopeGroup> findByServiceOrderByOrderTree(Services service);
}