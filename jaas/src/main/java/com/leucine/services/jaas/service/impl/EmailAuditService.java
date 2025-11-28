package com.leucine.services.jaas.service.impl;

import com.leucine.services.jaas.util.IdGenerator;
import com.leucine.services.jaas.dto.PreparedEmail;
import com.leucine.services.jaas.model.EmailAudit;
import com.leucine.services.jaas.repository.IEmailAuditRepository;
import com.leucine.services.jaas.service.IEmailAuditService;
import com.leucine.services.jaas.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuditService implements IEmailAuditService {
  private final IEmailAuditRepository repository;

  @Override
  @Async("auditExecutor")
  public void email(PreparedEmail preparedEmail) {
    EmailAudit emailAudit = new EmailAudit();
    emailAudit.setId(IdGenerator.getInstance().nextId());
    emailAudit.setFromAddress(preparedEmail.getFrom());
    if (!Utility.isEmpty(preparedEmail.getBcc())) {
      emailAudit.setBcc(preparedEmail.getBcc());
    }
    if (!Utility.isEmpty(preparedEmail.getCc())) {
      emailAudit.setCc(preparedEmail.getCc());
    }
    if (!Utility.isEmpty(preparedEmail.getTo())) {
      emailAudit.setToAddresses(preparedEmail.getTo());
    }
    emailAudit.setSubject(preparedEmail.getSubject());
    emailAudit.setBody(preparedEmail.getBody());
    repository.save(emailAudit);
  }
}
