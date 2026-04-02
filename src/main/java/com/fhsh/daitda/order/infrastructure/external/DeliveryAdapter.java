package com.fhsh.daitda.order.infrastructure.external;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fhsh.daitda.order.application.client.CompanyClient;
import com.fhsh.daitda.order.application.client.DeliveryClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveryAdapter implements DeliveryClient {
	private final DeliveryFeignClient deliveryFeignClient;

	public UUID createDelivery(UUID orderId, UUID supplierCompanyId, UUID receiverCompanyId) {
		DeliveryRequestDto.Creation requestDto = new DeliveryRequestDto.Creation(orderId, supplierCompanyId, receiverCompanyId);
		return deliveryFeignClient.createDelivery(requestDto);
	}
}
