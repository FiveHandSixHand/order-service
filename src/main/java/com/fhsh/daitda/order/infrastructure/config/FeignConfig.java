package com.fhsh.daitda.order.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.fhsh.daitda.order.infrastructure.external")
public class FeignConfig {
}
