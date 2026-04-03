package com.fhsh.daitda.order.infrastructure.external;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fhsh.daitda.response.CommonResponse;

@FeignClient(name = "hub-service")
public interface HubInventoryFeignClient{
	@PatchMapping("/internal/v1/hub-inventories/decrease")
	CommonResponse<HubInventoryResponseDto.Decrease> decreaseHubInventory(@RequestBody HubInventoryRequestDto.Decrease requestDto);
	@PatchMapping("/internal/v1/hub-inventories/restoration")
	CommonResponse<Void> restoreHubInventory(@RequestBody HubInventoryRequestDto.Restoration requestDto);
}
