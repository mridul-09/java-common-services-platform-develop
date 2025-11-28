package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.PasswordPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPasswordPolicyRepository extends JpaRepository<PasswordPolicy, Long> {

  @Query(com.leucine.services.jaas.constant.Query.GET_PASSWORD_POLICY)
  List<PasswordPolicy> getAll();

  PasswordPolicy findByOrganisationId(Long organisationId);

}
