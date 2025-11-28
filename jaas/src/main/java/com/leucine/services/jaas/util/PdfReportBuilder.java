package com.leucine.services.jaas.util;

import com.leucine.services.jaas.constant.Severity;
import com.leucine.services.jaas.dto.AuditDateGroupDto;
import com.leucine.services.jaas.dto.AuditEntryDto;
import com.leucine.services.jaas.dto.AuditReportDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PdfReportBuilder {

    // HTML Template Constants
    private static final String DATE_SECTION_TEMPLATE = 
        "<section class=\"date-section\">%s – %d activities%s</section>\n";
    
    private static final String CRITICAL_COUNT_TEMPLATE = " – %d %s";
    
    private static final String ACTIVITY_LIST_START = "<ul class=\"activities\">";
    private static final String ACTIVITY_LIST_END = "</ul>\n";
    
    private static final String ACTIVITY_ITEM_TEMPLATE = 
        "<li%s><span class=\"circle\"></span><span class=\"time\">%s</span><span class=\"details\">%s</span></li>";
    
    private static final String CRITICAL_CLASS = " class=\"critical\"";

    /**
     * Builds the audit section HTML content for PDF reports
     * @param auditData the audit report data containing date-grouped audit entries
     * @return HTML string for the audit section content
     */
    public String buildAuditSection(AuditReportDataDto auditData) {
        StringBuilder sb = new StringBuilder();

        // Process audit date groups (in database order)
        auditData.getAuditDateGroups().forEach(dateGroup -> {
            // Build date section using template with pre-calculated counts
            String criticalText = dateGroup.getCriticalCount() > 0 ? 
                String.format(CRITICAL_COUNT_TEMPLATE, dateGroup.getCriticalCount(), 
                    dateGroup.getCriticalCount() == 1 ? "Critical" : "Criticals") : "";
            sb.append(String.format(DATE_SECTION_TEMPLATE, dateGroup.getDate(), 
                dateGroup.getTotalCount(), criticalText));

            // Start activity list
            sb.append(ACTIVITY_LIST_START);

            // Build activity items using template
            dateGroup.getAuditEntries().forEach(auditEntry -> {
                boolean isCrit = Severity.CRITICAL.equals(auditEntry.getSeverity());
                String cssClass = isCrit ? CRITICAL_CLASS : "";
                sb.append(String.format(ACTIVITY_ITEM_TEMPLATE, cssClass, 
                    auditEntry.getTimestamp(), auditEntry.getDetails()));
            });

            // End activity list
            sb.append(ACTIVITY_LIST_END);
        });

        return sb.toString();
    }
}
