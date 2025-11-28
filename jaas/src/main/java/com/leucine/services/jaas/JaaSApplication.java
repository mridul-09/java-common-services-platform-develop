package com.leucine.services.jaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class JaaSApplication {
  // TODO Add a custom Spring Formatter to automatically convert String to SearchFilter
  public static void main(String[] args) {
    SpringApplication.run(JaaSApplication.class, args);
  }

  @Bean(name = "auditRestTemplate")
  public RestTemplate auditRestTemplate() {
    return new RestTemplate();
  }
}
