package com.fhsh.daitda.order.infrastructure.external.company;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fhsh.daitda.order.application.client.CompanyClient;
import com.fhsh.daitda.response.CommonResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompanyAdapter implements CompanyClient{
	private final CompanyFeignClient companyFeignClient;

	@Override
	public Map<UUID, String> getProductNames(UUID supplierCompanyId, List<UUID> productIds) {
		CommonResponse<Map<String, String>> response = companyFeignClient.getProductNames(productIds);
		if (response == null || response.getData() == null) {
			throw new RuntimeException("상품 서비스로부터 데이터를 받지 못했습니다.");
		}

		return response.getData().entrySet().stream()
			.collect(Collectors.toMap(
				entry -> UUID.fromString(entry.getKey()), // String 키를 UUID로 변환
				Map.Entry::getValue                        // Value(상품명)는 그대로 유지
			));
	}
}
