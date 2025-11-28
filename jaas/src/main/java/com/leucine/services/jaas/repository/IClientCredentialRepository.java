package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.ClientCredential;
import com.leucine.services.jaas.model.ClientFacilityMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IClientCredentialRepository extends JpaRepository<ClientCredential, String> {

  @Query("SELECT cfm FROM ClientFacilityMapping cfm WHERE cfm.clientFacilityCompositeId.clientCredential.id = :clientId")
  List<ClientFacilityMapping> findByClientId(@Param("clientId") String clientId);

}
