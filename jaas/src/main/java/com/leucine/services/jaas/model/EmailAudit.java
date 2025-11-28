package com.leucine.services.jaas.model;

import com.leucine.services.jaas.util.DateTimeUtils;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "email_audits")
@TypeDefs({
  @TypeDef(
    name = "string-array",
    typeClass = StringArrayType.class
  )
})
public class EmailAudit {
  @Id
  @Column(name = "id", columnDefinition = "bigint", updatable = false, nullable = false)
  private Long id;

  @Column(name = "from_address", columnDefinition = "text", nullable = false)
  private String fromAddress;

  @Column(name = "to_addresses", columnDefinition = "text[]")
  @Type(type = "string-array")
  private String[] toAddresses;

  @Column(name = "cc", columnDefinition = "text[]")
  @Type(type = "string-array")
  private String[] cc;

  @Column(name = "bcc", columnDefinition = "text[]")
  @Type(type = "string-array")
  private String[] bcc;

  @Column(name = "body", columnDefinition = "text")
  private String body;

  @Column(name = "subject", columnDefinition = "text")
  private String subject;

  @Column(name = "retry_attempts", columnDefinition = "smallint")
  private short retryAttempts = 0;

  @Column(name = "max_attempts", columnDefinition = "smallint")
  private short maxAttempts = 3;

  @Column(name = "created_on", columnDefinition = "bigint")
  private Long createdOn;

  @PrePersist
  public void beforePersist() {
    createdOn = DateTimeUtils.now();
  }

}
