package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.ClientFacilityMapping;
import com.leucine.services.jaas.model.composite.ClientFacilityCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClientFacilityMappingRepository extends JpaRepository<ClientFacilityMapping, ClientFacilityCompositeKey> {

}
