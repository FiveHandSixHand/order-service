package com.fhsh.daitda.order.infrastructure.external.company;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fhsh.daitda.response.CommonResponse;

@FeignClient(name = "product-service")
public interface CompanyFeignClient {
	@PostMapping("/api/v1/products/names-by-ids")
	CommonResponse<Map<String, String>> getProductNames(@RequestBody List<UUID> productIds);
}
