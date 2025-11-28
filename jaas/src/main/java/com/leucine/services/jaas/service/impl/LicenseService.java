package com.leucine.services.jaas.service.impl;

import com.leucine.services.jaas.constant.State;
import com.leucine.services.jaas.dto.LicenseDto;
import com.leucine.services.jaas.model.License;
import com.leucine.services.jaas.repository.ILicenseRepository;
import com.leucine.services.jaas.service.ILicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LicenseService implements ILicenseService {

  private final ILicenseRepository repository;

  @Override
  public List<LicenseDto> getDetails(List<Long> facilityIds) {
    List<License> licensesDetails = repository.getUnarchivedLicenseByFacilityIds(facilityIds);
    List<LicenseDto> licenses = new ArrayList<>();
    for (License license : licensesDetails) {
      long days;
      final LocalDate today = LocalDate.now();
      final LocalDate subscriptionRenewalDate = license.getSubscriptionRenewalDate();
      final boolean expired = !license.isPaymentDone();
      final LocalDate graceEndsOn = license.getSubscriptionRenewalDate().plusDays(license.getGracePeriod());
      final LocalDate intimateOn = license.getSubscriptionRenewalDate().minusDays(license.getIntimateBefore());
      boolean grace = expired && graceEndsOn.compareTo(today) > 0;
      boolean graceExceeded = expired && graceEndsOn.compareTo(today) < 0;
      boolean intimate = !expired && !grace && intimateOn.compareTo(today) < 0;
      State.License state = State.License.GENUINE;
      if (intimate) {
        state = State.License.INTIMATE;
      } else if (grace) {
        state = State.License.GRACE;
      } else if (graceExceeded) {
        state = State.License.GRACE_EXCEEDED;
      }
      if (state == State.License.GENUINE) {
        final LocalDate previousSubscriptionRenewalDate = license.getSubscriptionRenewalDate().minusDays(license.getSubscriptionPeriod());
        days = Math.abs(Duration.between(previousSubscriptionRenewalDate.atStartOfDay(), today.atStartOfDay()).toDays());
      } else {
        days = Math.abs(Duration.between(subscriptionRenewalDate.atStartOfDay(), today.atStartOfDay()).toDays());
      }
      licenses.add((new LicenseDto()).setExpired(expired)
        .setFacilityId(license.getFacilityId().toString())
        .setRenewalDate(subscriptionRenewalDate)
        .setGraceEndsOn(graceEndsOn)
        .setState(state)
        .setDays(days)
        .setWorkflow(license.getWorkflow().toString()));
    }
    return licenses;
  }
}
