package com.leucine.services.jaas.service;

import com.leucine.services.jaas.dto.LicenseDto;

import java.util.List;

public interface ILicenseService {
  List<LicenseDto> getDetails(List<Long> facilityIds);
}
