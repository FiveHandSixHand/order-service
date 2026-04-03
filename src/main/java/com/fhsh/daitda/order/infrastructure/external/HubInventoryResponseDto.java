package com.fhsh.daitda.order.infrastructure.external;

import java.util.List;
import java.util.UUID;

public class HubInventoryResponseDto {
	public record Decrease(
		List<InventoryResult> items
	) {
		public record InventoryResult(
			UUID hubInventoryId,
			UUID productId
		) {}
	}
}
