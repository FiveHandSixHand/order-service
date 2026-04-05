package com.fhsh.daitda.order.infrastructure.external.company;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "company-service")
public interface CompanyFeignClient {
	@PostMapping("/internal/v1/companies/{companyId}")
	Map<UUID, String> getProductNames(@PathVariable("companyId") UUID supplierCompanyId, @RequestBody List<UUID> productIds);
}
