package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.AuditBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.FACILITIES)
public class Facility extends AuditBaseEntity implements Serializable {
  public static final String ORGANISATION_ID = "organisationId";
  private static final long serialVersionUID = 262860152711667404L;

  @Column(name = "name", columnDefinition = "varchar", length = 150)
  private String name;

  @JsonIgnore
  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organisations_id", nullable = false, insertable = false, updatable = false)
  private Organisation organisation;

  @Column(name = "organisations_id", columnDefinition = "bigint", updatable = false, insertable = false)
  @Type(type = "org.hibernate.type.LongType")
  private Long organisationId;

  @Column(name = "time_format", columnDefinition = "varchar")
  private String timeFormat;

  @Column(name = "date_format", columnDefinition = "varchar")
  private String dateFormat;

  @Column(name = "date_time_format", columnDefinition = "varchar")
  private String dateTimeFormat;

  @Column(name = "time_zone", columnDefinition = "varchar")
  private String timeZone;

  @Column(name = "logo_url", columnDefinition = "varchar", length = 255)
  private String logoUrl;

  public Facility(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Facility(Long id, String name, String dateFormat, String timeFormat, String dateTimeFormat, String timeZone, String logoUrl) {
    this.id = id;
    this.name = name;
    this.dateFormat = dateFormat;
    this.timeFormat = timeFormat;
    this.dateTimeFormat = dateTimeFormat;
    this.timeZone = timeZone;
    this.logoUrl = logoUrl;
  }
}
