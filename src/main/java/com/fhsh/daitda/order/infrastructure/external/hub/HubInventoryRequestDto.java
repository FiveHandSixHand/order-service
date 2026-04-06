package com.fhsh.daitda.order.infrastructure.external.hub;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HubInventoryRequestDto {
	public record Decrease(
		UUID supplierCompanyId,
		@JsonProperty("orderItems") List<Item> items
	) {}

	public record Restoration(
		@JsonProperty("orderItems") List<RestoreItem> restoreItems
	) {}

	public record Item(
		UUID productId,
		int quantity
	) {}

	public record RestoreItem(
		UUID hubInventoryId,
		int quantity
	) {}
}
