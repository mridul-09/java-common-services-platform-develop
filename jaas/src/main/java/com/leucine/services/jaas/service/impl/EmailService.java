package com.leucine.services.jaas.service.impl;

import com.leucine.services.jaas.config.EmailProperties;
import com.leucine.services.jaas.dto.EmailRequest;
import com.leucine.services.jaas.dto.PreparedEmail;
import com.leucine.services.jaas.exception.EmailException;
import com.leucine.services.jaas.service.IEmailAuditService;
import com.leucine.services.jaas.service.IEmailService;
import com.leucine.services.jaas.util.Utility;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class EmailService implements IEmailService {
  private final JavaMailSender mailSender;
  private final Configuration freemarkerConfig;
  private final EmailProperties emailProperties;
  private final IEmailAuditService emailAuditService;
  private final PolicyFactory emailAttributeSanitizerPolicy;
  private final RestTemplate restTemplate;


  @Autowired
  public EmailService(final JavaMailSender javaMailSender, final Configuration configuration,
                      final EmailProperties emailProperties, final IEmailAuditService emailAuditService) {
    this.mailSender = javaMailSender;
    this.freemarkerConfig = configuration;
    this.emailProperties = emailProperties;
    this.emailAuditService = emailAuditService;

    this.emailAttributeSanitizerPolicy = new HtmlPolicyBuilder()
      .allowElements() // No elements allowed
      .toFactory();
    this.restTemplate = new RestTemplate();
  }

  @Override
  @Async("emailExecutor")
  public void sendEmail(EmailRequest emailRequest) {
    try {
      if (null != emailRequest) {
        sendMail(emailRequest);
      }
    } catch (EmailException e) {
      e.printStackTrace();
    }
  }

  private boolean sendMail(EmailRequest emailRequest) throws EmailException {
    try {
      var preparedEmail = prepareMail(emailRequest);
      mailSender.send(prepareMimeMessage(preparedEmail));
      emailAuditService.email(preparedEmail);
    } catch (IOException | TemplateException | MessagingException ex) {
      // TODO: Add logger
      // NOTE: IOException & TemplateException are thrown by FreeMarker wheres MessagingException by JavaMailSender
      ex.printStackTrace();
      throw new EmailException(ex);
    }
    return true;
  }

  private PreparedEmail prepareMail(EmailRequest emailRequest) throws IOException, TemplateException {
    var preparedEmail = new PreparedEmail();
    String fromAddress = preparedEmail.getFrom() == null ? emailProperties.getFromAddress() : preparedEmail.getFrom();
    preparedEmail.setFrom(fromAddress);
    if (null != emailRequest.getTo()) {
      preparedEmail.setTo(emailRequest.getTo().stream().filter(Objects::nonNull).toArray(String[]::new));
    }
    if (null != emailRequest.getCc()) {
      preparedEmail.setCc(emailRequest.getCc().stream().filter(Objects::nonNull).toArray(String[]::new));
    }
    if (null != emailRequest.getBcc()) {
      preparedEmail.setBcc(emailRequest.getBcc().stream().filter(Objects::nonNull).toArray(String[]::new));
    }
    preparedEmail.setSubject(emailRequest.getSubject());

    // Sanitize each attribute value in the map.
    Map<String, String> sanitizedAttributes = new HashMap<>();
    for (Map.Entry<String, String> entry : emailRequest.getAttributes().entrySet()) {
      String sanitizedValue = emailAttributeSanitizerPolicy.sanitize(entry.getValue());
      sanitizedAttributes.put(entry.getKey(), sanitizedValue);
    }

    var template = freemarkerConfig.getTemplate(emailRequest.getTemplateName());
    var html = FreeMarkerTemplateUtils.processTemplateIntoString(template, sanitizedAttributes);
    preparedEmail.setBody(html);
    return preparedEmail;
  }

  private MimeMessage prepareMimeMessage(PreparedEmail preparedEmail) throws MessagingException {
    var message = mailSender.createMimeMessage();
    var helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
    helper.setFrom(preparedEmail.getFrom());
    if (null != preparedEmail.getTo()) {
      helper.setTo(preparedEmail.getTo());
    }
    if (null != preparedEmail.getCc()) {
      helper.setCc(preparedEmail.getCc());
    }
    if (null != preparedEmail.getBcc()) {
      helper.setBcc(preparedEmail.getBcc());
    }
    helper.setText(preparedEmail.getBody(), true);
    helper.setSubject(preparedEmail.getSubject());
    //TODO host image to avoid no image show in emails ?


    if (Utility.isEmpty(emailProperties.getLogoUrl())) {
      helper.addInline("leucine-blue-logo", new ClassPathResource("leucine-blue-logo.png"));
    } else {
      byte[] imageBytes = restTemplate.getForObject(emailProperties.getLogoUrl(), byte[].class);
      ByteArrayResource imageResource = new ByteArrayResource(imageBytes);
      helper.addInline("leucine-blue-logo", imageResource, "image/png");
    }

    return message;
  }

}

