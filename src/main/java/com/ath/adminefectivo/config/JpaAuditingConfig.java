package com.ath.adminefectivo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Activar la funcionalidad de auditoria
 * @author prv_nparra
 * @since 2025
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
  
  @Bean
  AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }

}
