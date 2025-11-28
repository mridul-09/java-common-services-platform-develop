package com.leucine.services.jaas.dto.request;

import com.leucine.services.jaas.constant.Misc;
import lombok.Data;

@Data
public class NotifyAdminRequest {
  private String identity;
  private Misc.NotifyAdminPurpose purpose;
}
