package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IUserAuditRepository extends JpaRepository<UserAudit, Long>, JpaSpecificationExecutor<UserAudit> {
}
