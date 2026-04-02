package com.fhsh.daitda.order.infrastructure.external;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "hub-service")
public interface HubInventoryFeignClient{
	@PatchMapping("/internal/v1/hub-inventories/decrease")
	boolean decreaseHubInventory(@RequestBody HubInventoryRequestDto.Decrease requestDto);
	@PatchMapping("/internal/v1/hub-inventories/restoration")
	boolean restoreHubInventory(@RequestBody HubInventoryRequestDto.Restoration requestDto);
}
