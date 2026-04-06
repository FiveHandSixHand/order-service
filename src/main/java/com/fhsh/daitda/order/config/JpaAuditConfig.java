package com.fhsh.daitda.order.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "headerAuditorAware")
@Configuration
public class JpaAuditConfig {
	@Bean
	public AuditorAware<UUID> auditorProvider(HeaderAuditorAware auditorAwareImpl) {
		return auditorAwareImpl;
	}
}
