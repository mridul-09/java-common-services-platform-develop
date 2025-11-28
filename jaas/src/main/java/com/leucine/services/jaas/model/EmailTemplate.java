package com.leucine.services.jaas.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "email_templates")
public class EmailTemplate implements Serializable {
  private static final long serialVersionUID = -7076912044397972740L;

  @Id
  @Column(name = "id", columnDefinition = "int", updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", columnDefinition = "varchar", length = 255, nullable = false)
  private String name;

  @Column(name = "content", columnDefinition = "text", nullable = false)
  private String content;
}
