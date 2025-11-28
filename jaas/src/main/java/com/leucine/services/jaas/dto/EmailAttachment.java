package com.leucine.services.jaas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailAttachment {
  private String filename;
  private byte[] data;
}
