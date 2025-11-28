package com.leucine.services.jaas.config;

import com.leucine.services.jaas.model.EmailTemplate;
import com.leucine.services.jaas.repository.IEmailTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class FreemarkerConfig {

  @Autowired
  private IEmailTemplateRepository IEmailTemplateRepository;

  @Primary
  @Bean
  public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
    FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
    // Create template loader
    StringTemplateLoader templateLoader = new StringTemplateLoader();
    // Find all templates
    Iterable<EmailTemplate> ite = IEmailTemplateRepository.findAll();
    ite.forEach((template) -> {
      templateLoader.putTemplate(template.getName(), template.getContent());
    });
    bean.setPreTemplateLoaders(templateLoader);
    return bean;
  }
}
