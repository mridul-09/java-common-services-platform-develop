package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IFacilityRepository extends JpaRepository<Facility, Long>, JpaSpecificationExecutor<Facility> {
  List<Facility> findAllByOrganisationId(Long organisationId);
}
