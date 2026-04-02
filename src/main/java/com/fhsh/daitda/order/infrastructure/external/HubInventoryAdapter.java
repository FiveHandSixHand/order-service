package com.fhsh.daitda.order.infrastructure.external;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fhsh.daitda.order.application.client.DeliveryClient;
import com.fhsh.daitda.order.application.client.HubInventoryClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HubInventoryAdapter implements HubInventoryClient {
	private final HubInventoryFeignClient hubInventoryFeignClient;

	@Override
	public boolean decreaseHubInventory(UUID supplierCompanyId, UUID productId, int quantity) {
		HubInventoryRequestDto.Decrease hubInventoryDto = new HubInventoryRequestDto.Decrease(
			supplierCompanyId,
			productId,
			quantity
		);
		return hubInventoryFeignClient.decreaseHubInventory(hubInventoryDto);
	}

	@Override
	public boolean restoreHubInventory(UUID hubInventoryId, int quantity) {
		HubInventoryRequestDto.Restoration hubInventoryDto = new HubInventoryRequestDto.Restoration(
			hubInventoryId,
			quantity
		);
		return hubInventoryFeignClient.restoreHubInventory(hubInventoryDto);
	}
}
