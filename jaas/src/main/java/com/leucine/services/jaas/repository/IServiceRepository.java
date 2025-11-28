package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.Services;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IServiceRepository extends JpaRepository<Services, String>, JpaSpecificationExecutor<Services> {

  @Override
  @EntityGraph(value = "service-role-scopeGroup-scope-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Services> findById(String id);

//  Optional<Services> findByName(String name);

//  @Query("SELECT s,r FROM Services s LEFT JOIN Role r on s.id = r.service WHERE s.id = :id")
//  Optional<Services> getDetailsById(@Param("id") String id);
}