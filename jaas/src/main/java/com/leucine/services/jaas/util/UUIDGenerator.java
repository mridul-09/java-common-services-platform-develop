package com.leucine.services.jaas.util;

import java.util.UUID;

public class UUIDGenerator {
  private static final UUIDGenerator INSTANCE = new UUIDGenerator();

  private UUIDGenerator() {
  }

  public static UUIDGenerator getInstance() {
    return INSTANCE;
  }

  public synchronized UUID nextId() {
    return UUID.randomUUID();
  }

  public String generateUnique() {
    return nextId().toString().replace("-", "");
  }
}
