package com.fhsh.daitda.order.infrastructure.external;

import java.util.List;
import java.util.UUID;

public class HubInventoryRequestDto {
	public record Decrease(
		UUID supplierCompanyId,
		List<Item> items
	) {}

	public record Restoration(
		UUID hubInventoryid,
		Integer quantity
	) {}

	public record Item(
		UUID productId,
		int quantity
	) {}
}
