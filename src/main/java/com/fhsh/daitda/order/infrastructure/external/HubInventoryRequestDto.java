package com.fhsh.daitda.order.infrastructure.external;

import java.util.UUID;

public class HubInventoryRequestDto {
	public record Decrease(
		UUID supplierCompanyId,
		UUID productId,
		Integer quantity
	) {}

	public record Restoration(
		UUID hubInventoryid,
		Integer quantity
	) {}
}
