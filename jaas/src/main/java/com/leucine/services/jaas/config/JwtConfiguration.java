package com.leucine.services.jaas.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtConfiguration implements Serializable {
  private static final long serialVersionUID = -3299936915300402206L;
  /**
   * Key is used to sign JwtToken.
   */
  private String secret;

  private Integer validationTokenExpirationInMinutes;

  private Integer accessTokenExpirationInMinutes;

  private Integer refreshTokenExpirationInMinutes;

  private Integer secretKeyLength;
}
