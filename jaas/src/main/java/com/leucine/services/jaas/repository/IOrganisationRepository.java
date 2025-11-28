package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IOrganisationRepository extends JpaRepository<Organisation, Long>, JpaSpecificationExecutor<Organisation> {
}
