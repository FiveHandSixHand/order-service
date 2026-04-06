package com.fhsh.daitda.order.infrastructure.external.hub;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HubInventoryRequestDto {
	public record Decrease(
		UUID supplierCompanyId,
		List<Item> orderItems
	) {}

	public record Restoration(
		@JsonProperty("orderItems")
		List<RestoreItem> restoreItems
	) {}

	protected record Item(
		UUID productId,
		int quantity
	) {}

	protected record RestoreItem(
		UUID hubInventoryId,
		int quantity
	) {}
}
