package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.constant.State;
import com.leucine.services.jaas.model.User;
import com.leucine.services.jaas.model.projection.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
  @Override
  Page<User> findAll(Specification<User> specification, Pageable pageable);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByUsernameOrEmail(String username, String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByEmployeeIdAndOrganisationId(String employeeId, Long organisationId);

  @Query(value = com.leucine.services.jaas.constant.Query.GET_ACTIVE_USERS_BY_ORGANISATION_ID_AND_ROLE_ID, nativeQuery = true)
  List<UserView> getAllByOrganisationIdAndRoleId(@Param("organisationId") Long organisationId, @Param("roleId") Long roleId);

  @EntityGraph(value = "user.facility")
  @Query(com.leucine.services.jaas.constant.Query.GET_USERS_WHOSE_LAST_UPDATED_PASSWORD_HAS_EXPIRED)
  List<User> getAllByExpiredPassword(@Param("organisationId") Long organisationId, @Param("offsetForPasswordUpdatedAtCalculation") Long offsetForPasswordUpdatedAtCalculation,
                                     @Param("now") Long now);

  @EntityGraph(value = "user.facility")
  @Query(com.leucine.services.jaas.constant.Query.GET_USERS_WHOSE_LOCKED_AT_HAS_EXCEEDED_AUTO_UNLOCK)
  List<User> getAllByLocked(@Param("organisationId") Long organisationId, @Param("offsetForLockedAtCalculation") Long offsetForLockedAtCalculation,
                            @Param("now") Long now);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(value = com.leucine.services.jaas.constant.Query.UPDATE_USER_STATE)
  void updateState(@Param("state") State.User state, @Param("userIds") List<Long> userIds);

  @Query(value = com.leucine.services.jaas.constant.Query.FIND_ROLE_NAME_BY_EMPLOYEE_ID, nativeQuery = true)
  String findRoleNameByEmployeeId(@Param("employeeId") String employeeId);
}
