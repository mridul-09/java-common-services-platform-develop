package com.leucine.services.jaas.repository;

import com.leucine.services.jaas.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
}
