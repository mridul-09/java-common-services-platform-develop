package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.model.UserFacilityMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface IUserFacilityMappingRepository extends JpaRepository<UserFacilityMapping, Long> {
  @Transactional
  void deleteByUser(User user);
}
