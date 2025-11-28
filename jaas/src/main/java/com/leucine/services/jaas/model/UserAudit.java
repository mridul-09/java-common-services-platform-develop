package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.Action;
import com.leucine.services.jaas.constant.Severity;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.BaseEntity;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Accessors(chain = true)
@Table(name = TableName.USER_AUDITS, indexes = {@Index(columnList = "organisations_id"), @Index(columnList = "organisations_id, triggered_by")})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "text-array", typeClass = StringArrayType.class)
public class UserAudit extends BaseEntity implements Serializable {
  public static final String DEFAULT_SORT = "triggeredAt";
  public static final String ORGANISATION_ID = "organisationId";
  public static final String FACILITY_ID = "facilityIds";

  private static final long serialVersionUID = -886697022015347210L;

  @Column(name = "organisations_id", columnDefinition = "bigint", updatable = false)
  private Long organisationId;

  // TODO migrate from text-array (StringArrayType) to long-array (LongArrayType) and text[] to bigint[]
  @Type(type = "text-array")
  @Column(name = "facility_ids", columnDefinition = "text[]", updatable = false)
  private String[] facilityIds;

  @Column(name = "triggered_by", columnDefinition = "bigint", nullable = false)
  private Long triggeredBy;

  @Column(name = "action", columnDefinition = "varchar", length = 255)
  @Enumerated(EnumType.STRING)
  private Action.UserAudit action;

  @Column(name = "severity", columnDefinition = "varchar", length = 255)
  @Enumerated(EnumType.STRING)
  private Severity severity;

  @Column(name = "details", columnDefinition = "text")
  private String details;

  @Column(name = "triggered_at", columnDefinition = "bigint")
  private Long triggeredAt;

}
