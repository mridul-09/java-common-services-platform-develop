package com.leucine.services.jaas.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.leucine.services.jaas.dto.OrganisationExtraDto;
import com.leucine.services.jaas.model.OrganisationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrganisationSettingRepository extends JpaRepository<OrganisationSetting, Long> {

  @Query(com.leucine.services.jaas.constant.Query.GET_ORGANISATION_SETTINGS)
  List<OrganisationSetting> getAll();

  OrganisationSetting findByOrganisationId(@Param("organisationId") Long organisationId);
}
