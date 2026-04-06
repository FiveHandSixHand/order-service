package com.fhsh.daitda.order.infrastructure.external.delivery;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fhsh.daitda.response.CommonResponse;

@FeignClient(name = "delivery-service")
public interface DeliveryFeignClient {
	@PostMapping("/internal/v1/deliveries")
	CommonResponse<DeliveryResponseDto.CreateDelivery> createDelivery(@RequestBody DeliveryRequestDto.Creation requestDto);
}
