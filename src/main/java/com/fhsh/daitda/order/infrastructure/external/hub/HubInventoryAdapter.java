package com.fhsh.daitda.order.infrastructure.external.hub;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fhsh.daitda.order.application.client.HubInventoryClient;
import com.fhsh.daitda.order.application.command.OrderItemCommand;
import com.fhsh.daitda.order.application.command.RestoreHubInventoryCommand;
import com.fhsh.daitda.response.CommonResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HubInventoryAdapter implements HubInventoryClient {
	private final HubInventoryFeignClient hubInventoryFeignClient;

	@Override
	public Map<UUID, UUID> decreaseHubInventory(UUID supplierCompanyId, List<OrderItemCommand> orderItems) {
		List<HubInventoryRequestDto.Item> items = orderItems.stream()
			.map(item -> new HubInventoryRequestDto.Item(item.productId(), item.quantity()))
			.toList();
		HubInventoryRequestDto.Decrease hubInventoryDto = new HubInventoryRequestDto.Decrease(
			supplierCompanyId,
			items
		);
		CommonResponse<HubInventoryResponseDto.Decrease> response = hubInventoryFeignClient.decreaseHubInventory(hubInventoryDto);

		return response.getData().items().stream()
			.collect(Collectors.toMap(
				HubInventoryResponseDto.Decrease.InventoryResult::productId,
				HubInventoryResponseDto.Decrease.InventoryResult::hubInventoryId
			));
	}

	@Override
	public void restoreHubInventory(List<RestoreHubInventoryCommand> hubInventoryCommands) {
		List<HubInventoryRequestDto.RestoreItem> restoreItems = hubInventoryCommands.stream().map(
			command -> new HubInventoryRequestDto.RestoreItem(
				command.hubInventoryId(),
				command.quantity()
			)).
			toList();
		HubInventoryRequestDto.Restoration hubInventoryDto = new HubInventoryRequestDto.Restoration(
			restoreItems
		);
		hubInventoryFeignClient.restoreHubInventory(hubInventoryDto);
	}
}
