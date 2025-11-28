package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.OrganisationServiceMapping;
import com.leucine.services.jaas.model.composite.OrganisationServiceCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrganisationServiceMappingRepository extends JpaRepository<OrganisationServiceMapping, OrganisationServiceCompositeKey>, JpaSpecificationExecutor<OrganisationServiceMapping> {
  OrganisationServiceMapping findByOrganisationIdAndServiceId(Long organisationId, String serviceId);

  @Query(value = com.leucine.services.jaas.constant.Query.FIND_ORGANISATION_ID_BY_FQDN, nativeQuery = true)
  Long findByFqdn(@Param("fqdn") String fqdn);
}
