package com.leucine.services.jaas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leucine.services.jaas.constant.TableName;
import com.leucine.services.jaas.dto.helper.IBaseEntity;
import com.leucine.services.jaas.model.helper.AuditOnlyBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.CHALLENGE_QUESTIONS)
public class ChallengeQuestion extends AuditOnlyBaseEntity implements IBaseEntity {
  public static final String ARCHIVED = "archived";
  private static final long serialVersionUID = 8982168532187711034L;

  @Id
  @Column(name = "id", columnDefinition = "int", updatable = false, nullable = false)
  private Integer id;

  @Column(name = "question", columnDefinition = "varchar", length = 512, nullable = false)
  private String question;

  @JsonIgnore
  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;
}