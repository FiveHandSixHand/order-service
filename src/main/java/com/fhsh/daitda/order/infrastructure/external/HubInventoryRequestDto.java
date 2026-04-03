package com.fhsh.daitda.order.infrastructure.external;

import java.util.List;
import java.util.UUID;

public class HubInventoryRequestDto {
	public record Decrease(
		UUID supplierCompanyId,
		List<Item> items
	) {}

	public record Restoration(
		List<RestoreItem> restoreItems
	) {}

	protected record Item(
		UUID productId,
		int quantity
	) {}

	protected record RestoreItem(
		UUID hubInventoryid,
		int quantity
	) {}
}
