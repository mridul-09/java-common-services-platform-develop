package com.leucine.services.jaas.service;

import com.leucine.services.jaas.dto.EmailRequest;

public interface IEmailService {
  void sendEmail(EmailRequest emailRequest);
}
