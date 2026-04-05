package com.fhsh.daitda.order.infrastructure.external.delivery;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fhsh.daitda.order.application.client.DeliveryClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveryAdapter implements DeliveryClient {
	private final DeliveryFeignClient deliveryFeignClient;

	public UUID createDelivery(UUID orderId, UUID supplierCompanyId, UUID receiverCompanyId) {
		try {
			DeliveryRequestDto.Creation requestDto = new DeliveryRequestDto.Creation(orderId, supplierCompanyId,
				receiverCompanyId);
			return deliveryFeignClient.createDelivery(requestDto);
		} catch (FeignException e) {
			throw e;
		}
	}
}
