package com.leucine.services.jaas.service;

import com.leucine.services.jaas.dto.PreparedEmail;

public interface IEmailAuditService {
  void email(PreparedEmail preparedEmail);
}
