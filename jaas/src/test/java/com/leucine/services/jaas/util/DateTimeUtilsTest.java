package com.leucine.services.jaas.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeUtilsTest {

  @Test
  void getCurrentYear() {
    assertEquals(String.valueOf(LocalDateTime.now().getYear()), DateTimeUtils.getCurrentYear());
  }

  @Test
  void getCurrentMonth() {
    assertEquals(String.format("%02d", LocalDateTime.now().getMonthValue()), DateTimeUtils.getCurrentMonth());
  }
}