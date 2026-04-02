package com.fhsh.daitda.order.infrastructure.external;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service")
public interface DeliveryFeignClient {
	@PostMapping("/internal/v1/deliveries")
	UUID createDelivery(@RequestBody DeliveryRequestDto.Creation requestDto);
}
