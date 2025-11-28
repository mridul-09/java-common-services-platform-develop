package com.leucine.services.jaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditDateGroupDto {
    private String date;
    private List<AuditEntryDto> auditEntries;
    private long criticalCount;
    private int totalCount;
}
