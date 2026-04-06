package com.fhsh.daitda.order.infrastructure.external.delivery;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fhsh.daitda.order.application.client.DeliveryClient;
import com.fhsh.daitda.order.application.client.dto.CreateDeliveryClientResponse;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveryAdapter implements DeliveryClient {
	private final DeliveryFeignClient deliveryFeignClient;

	public CreateDeliveryClientResponse createDelivery(UUID orderId, UUID supplierCompanyId, UUID receiverCompanyId) {
		try {
			DeliveryRequestDto.Creation requestDto = new DeliveryRequestDto.Creation(orderId, supplierCompanyId,
				receiverCompanyId);
			DeliveryResponseDto.CreateDelivery delivery = deliveryFeignClient.createDelivery(requestDto).getData();
			return new CreateDeliveryClientResponse(
				delivery.deliveryId(),
				delivery.deliveryManagersId()
			);
		} catch (FeignException e) {
			throw e;
		}
	}
}
