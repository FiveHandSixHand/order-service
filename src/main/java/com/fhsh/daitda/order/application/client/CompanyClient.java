package com.fhsh.daitda.order.application.client;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CompanyClient {
	Map<UUID, String> getProductNames(UUID supplierCompanyId, List<UUID> productIds);
}
