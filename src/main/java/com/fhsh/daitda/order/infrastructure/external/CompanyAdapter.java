package com.fhsh.daitda.order.infrastructure.external;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fhsh.daitda.order.application.client.CompanyClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompanyAdapter implements CompanyClient{
	@Autowired
	private final CompanyFeignClient companyFeignClient;

	@Override
	public Map<UUID, String> getProductNames(UUID supplierCompanyId, List<UUID> productIds) {
		return companyFeignClient.getProductNames(supplierCompanyId, productIds);
	}
}
