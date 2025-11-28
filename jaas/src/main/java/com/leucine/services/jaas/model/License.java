package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.model.helper.AuditBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.LICENSES)
public class License extends AuditBaseEntity implements Serializable {
  public static final String FACILITY_ID = "facilities.facility.id";
  private static final long serialVersionUID = -3286288007485045125L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organisations_id", referencedColumnName = "id", nullable = false)
  private Organisation organisation;

  @Column(name = "organisations_id", columnDefinition = "bigint", updatable = false, insertable = false)
  @Type(type = "org.hibernate.type.LongType")
  private Long organisationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "facilities_id", referencedColumnName = "id", nullable = false)
  private Facility facility;

  @Column(name = "facilities_id", columnDefinition = "bigint", updatable = false, insertable = false)
  @Type(type = "org.hibernate.type.LongType")
  private Long facilityId;

  @Column(name = "product", columnDefinition = "varchar", length = 150)
  private String product;

  @Column(name = "type", columnDefinition = "varchar", length = 150)
  private String type;

  @Column(name = "subscription_start_date", columnDefinition = "date")
  private LocalDate subscriptionStartDate;

  // In days
  @Column(name = "subscription_period", columnDefinition = "int")
  private Integer subscriptionPeriod;

  @Column(name = "subscription_renewal_date", columnDefinition = "date")
  private LocalDate subscriptionRenewalDate;

  @Column(name = "payment_done", columnDefinition = "boolean")
  private boolean paymentDone;

  // In days
  @Column(name = "intimate_before", columnDefinition = "int")
  private Integer intimateBefore;

  // In days
  @Column(name = "grace_period", columnDefinition = "int")
  private Integer gracePeriod;

  @Column(name = "workflow", columnDefinition = "varchar", length = 150)
  @Enumerated(value = EnumType.STRING)
  private Misc.LicenseWorkflow workflow;

  @JsonIgnore
  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;

}
