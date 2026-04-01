package com.fhsh.daitda.order.infrastructure.external;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CompanyFeignClient {
	Map<UUID, String> getProductNames(UUID supplierCompanyId, List<UUID> productIds);
}
