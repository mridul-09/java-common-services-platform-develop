package com.leucine.services.jaas.controller.impl;

import com.leucine.services.jaas.config.VersionProperties;
import com.leucine.services.jaas.controller.IVersionController;
import com.leucine.services.jaas.dto.VersionDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VersionController implements IVersionController {

  private final VersionProperties properties;

  @Override
  public VersionDto get() {
    return VersionDto.builder().version(properties.getVersion()).branch(properties.getBranch()).commit(properties.getCommit()).build();
  }
}