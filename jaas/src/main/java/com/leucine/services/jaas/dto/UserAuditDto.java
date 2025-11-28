package com.leucine.services.jaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.leucine.services.jaas.constant.Action;
import com.leucine.services.jaas.constant.Severity;
import com.leucine.services.jaas.constant.Type;
import com.leucine.services.jaas.dto.helper.IBaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserAuditDto implements IBaseDto, Serializable {
  private static final long serialVersionUID = 5706312654169168062L;
  private Type.Object object = Type.Object.AUDIT_TRAILS;
  private String id;
  private Action.UserAudit action;
  private Severity severity;
  private JsonNode oldData;
  private JsonNode newData;
  private JsonNode diffData;
  private String details;
  private Long triggeredAt;
  private String triggeredBy;

  @JsonIgnore
  private long organisationId;
}
