package com.leucine.services.jaas.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class DateTimeUtils {

  private static final String DEFAULT_FORMAT = "MMM dd, YYYY";

  private DateTimeUtils() {
    throw new IllegalStateException("DateTimeUtils class");
  }

  public static String getCurrentYear() {
    return formatDate(now(), "YYYY");
  }

  public static String getCurrentDay() {
    return formatDate(now(), "dd");
  }

  public static String getCurrentMonth() {
    return formatDate(now(), "MM");
  }

  public static long now() {
    return Instant.now().getEpochSecond();
  }

  /**
   * Returns epoch milliseconds with the specified duration in minutes added.
   *
   * @param minutes - the amount to add, not null
   * @return a minutes based on this instant with the specified minutes added, not null
   */
  public static long nowPlusMinutesToEpochMilli(Integer minutes) {
    return Instant.now().plus((null == minutes ? 0 : minutes), ChronoUnit.MINUTES).toEpochMilli();
//    return (Instant.now().getEpochSecond() + ((null == minutes ? 0 : minutes) * 60)) * 1000;
//    return (Instant.now().getEpochSecond() + ((null == minutes ? 0 : minutes) * 60)) / 60;
  }

  public static long nowPlusMinutesToEpochSeconds(Integer minutes) {
    return Instant.now().plus((null == minutes ? 0 : minutes), ChronoUnit.MINUTES).getEpochSecond();
  }

  public static long getSecondsFromMinutes(Integer minutes) {
    return minutes * 60L;
  }

  public static long getSecondsFromDays(Integer days) {
    return days * 24 * 60 * 60L;
  }

  public static long now(ZoneOffset zoneOffset) {
    return LocalDateTime.now().toEpochSecond(zoneOffset);
  }

  public static String getFormattedDate(String localDate) {
    if (!Utility.isEmpty(localDate)) {
      return DateTimeUtils.getFormattedDate(LocalDate.parse(localDate));
    }
    return null;
  }

  public static String getFormattedDate(LocalDate localDate) {
    return DateTimeUtils.formatDate(localDate, DateTimeUtils.DEFAULT_FORMAT);
  }

  private static String formatDate(LocalDate localDate, String format) {
    return localDate.format(DateTimeFormatter.ofPattern(format));
  }

  private static String formatDate(long now, String format) {
    return LocalDateTime.ofEpochSecond(now, 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(format));
  }

  public static long getEpoch(LocalDateTime localDateTime) {
    return localDateTime.toEpochSecond(ZoneOffset.UTC);
  }

  public static long getEpoch(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
    return localDateTime.toEpochSecond(zoneOffset);
  }

  public static LocalDateTime getLocalDataTime() {
    return LocalDateTime.now();
  }

  public static LocalDateTime getLocalDataTime(long epochSecond) {
    return LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);
  }

  public static LocalDateTime getLocalDataTime(long epochSecond, ZoneOffset zoneOffset) {
    return LocalDateTime.ofEpochSecond(epochSecond, 0, zoneOffset);
  }
}


