package com.leucine.services.jaas.util;

import com.leucine.services.jaas.dto.AuditReportDataDto;
import com.leucine.services.jaas.model.OrganisationSetting;
import com.leucine.services.jaas.repository.IOrganisationSettingRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class PdfGeneratorUtil {
  private static final String TEMPLATE_PATH = "jaas/src/main/resources/templates/user-audit-report.html";

  private final IOrganisationSettingRepository organisationSettingRepository;
  private final PdfReportBuilder pdfReportBuilder;

  @Value("${pdf.logo-url}")
  private String leucineLogo;



  public byte[] generate(String user,
                         String employee_id,
                         String facility,
                         Long organisationId,
                         AuditReportDataDto auditData) {

    String populatedHtml = readHtmlTemplate();
    String audit = pdfReportBuilder.buildAuditSection(auditData);
    
    // Fetch client logo from organization settings
    String clientLogo = getClientLogoFromOrganisation(organisationId);

    String finalHtml = populatedHtml
      .replace("{{CONTENT}}", audit)
      .replace("{{CLIENT_LOGO}}", clientLogo)
      .replace("{{LEUCINE_LOGO}}", leucineLogo)
      .replace("{{GENERATED_AT}}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")))
      .replace("{{USER}}", user)
      .replace("{{USER_ID}}", employee_id)
      .replace("{{FACILITY}}", facility);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      HtmlConverter.convertToPdf(finalHtml, out, new ConverterProperties());
      return out.toByteArray();
    } catch (IOException e) {
      log.error("PDF generation failed", e);
      throw new RuntimeException("Failed to create PDF", e);
    }
  }



  private String getClientLogoFromOrganisation(Long organisationId) {
    try {
      OrganisationSetting organisationSetting = organisationSettingRepository.findByOrganisationId(organisationId);
      if (organisationSetting != null && organisationSetting.getLogoUrl() != null) {
        return organisationSetting.getLogoUrl();
      }
      // Fallback to default logo if no organization logo is set
      return leucineLogo;
    } catch (Exception e) {
      log.warn("Failed to fetch organization logo for organisationId: {}, using default", organisationId, e);
      return leucineLogo;
    }
  }

  private String readHtmlTemplate() {
    try {
      Resource res = new ClassPathResource("templates/user-audit-report.html");
      return new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException ex) {
      log.error("Cannot read HTML template", TEMPLATE_PATH , ex.getMessage() , ex);
      return null;
    }
  }
}
