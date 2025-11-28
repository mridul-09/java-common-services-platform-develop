package com.leucine.services.jaas.dto;

import com.leucine.services.jaas.constant.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEntryDto {
    private String timestamp;
    private String details;
    private Severity severity;
}
