package com.leucine.services.jaas.sso;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUserDto implements Serializable {
  @Serial
  private static final long serialVersionUID = 2306194029815289888L;

  private String firstName;
  private String lastName;
  private String name;
  private String username;
  private String department;
  private String employeeId;
  private String email;
}
