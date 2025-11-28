package com.leucine.services.jaas.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PreparedEmail {
  private String from;
  private String[] to;
  private String[] cc;
  private String[] bcc;
  private String subject;
  private String body;
  private List<EmailAttachment> attachments;
  private Map<String, String> attributes;
}
