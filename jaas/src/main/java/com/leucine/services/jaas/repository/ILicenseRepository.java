package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ILicenseRepository extends JpaRepository<License, Long>, JpaSpecificationExecutor<License> {


  @Query(value = com.leucine.services.jaas.constant.Query.GET_LICENSE_BY_FACILITY_IDS)
  List<License> getUnarchivedLicenseByFacilityIds(@Param("facilityIds") List<Long> facilityIds);

  List<License> getAllByArchived(boolean archive);
}