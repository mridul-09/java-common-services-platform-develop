package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.model.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;

public interface IUserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long>, JpaSpecificationExecutor<UserRoleMapping> {

  @Transactional
  void deleteByUser(User user);
}
