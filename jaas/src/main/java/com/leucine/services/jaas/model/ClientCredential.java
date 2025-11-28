package com.leucine.services.jaas.model;

import com.leucine.services.jaas.constant.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = TableName.CLIENT_CREDENTIALS)
public class ClientCredential implements Serializable {
  @Serial
  private static final long serialVersionUID = 2306194029815289888L;
  private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  @Id
  @Column(columnDefinition = "varchar", updatable = false, nullable = false)
  protected String id;

  public String getIdAsString() {
    return String.valueOf(id);
  }

  @Column(name = "client_secret", columnDefinition = "varchar", length = 50, nullable = false)
  private String clientSecret;

  @Column(name = "client_name", columnDefinition = "varchar", length = 50, nullable = false)
  private String clientName;

  @Column(name = "client_description", columnDefinition = "varchar")
  private String clientDescription;

  @Column(name = "archived", columnDefinition = "boolean", nullable = false)
  private boolean archived = false;

  public ClientCredential setClientSecret(String clientSecret) {
    this.clientSecret = bCryptPasswordEncoder.encode(clientSecret);
    return this;
  }


}
