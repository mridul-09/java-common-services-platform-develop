package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.EmailAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmailAuditRepository extends JpaRepository<EmailAudit, Long> {
}
