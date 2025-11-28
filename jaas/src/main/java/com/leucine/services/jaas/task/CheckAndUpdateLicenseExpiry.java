package com.leucine.services.jaas.task;

import com.leucine.services.jaas.model.License;
import com.leucine.services.jaas.repository.ILicenseRepository;
import com.leucine.services.jaas.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckAndUpdateLicenseExpiry implements ITask, Serializable {
  private static final long serialVersionUID = -3773476011845795157L;

  private final transient ILicenseRepository licenseRepository;

  /**
   * Scheduler that run every day at 0000 hours to update the license details.
   * For all the license with payment done, if the subscription renewal has expired then set payment done to false.
   */
  @Scheduled(cron = "0 0 * * * *", zone = "Asia/Kolkata")
//  @Scheduled(cron = "1 * * * * *", zone = "Asia/Kolkata")
  public void run() {
    log.info("[run] Check and update license expiry process started.");
    List<License> licenses = licenseRepository.getAllByArchived(false);
    final LocalDate today = LocalDate.now();
    for (License license : licenses) {
      log.info("[run] License details, Id [{}]", license.getId());
      boolean update = false;
      if (license.isPaymentDone() && license.getSubscriptionRenewalDate().compareTo(today) < 0) {
        log.info("[run] Updating the license details for Id [{}]", license.getId());
        update = true;
        license.setPaymentDone(false);
        license.setModifiedAt(DateTimeUtils.now());
      }
      if (update) {
        licenseRepository.save(license);
      }
    }
    log.info("[run] Check and update license expiry process completed.");
  }
}
