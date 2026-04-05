package com.fhsh.daitda.order.application.client;

import java.util.UUID;

import com.fhsh.daitda.order.application.client.dto.CreateDeliveryClientResponse;

public interface DeliveryClient {
	CreateDeliveryClientResponse createDelivery(UUID orderId, UUID supplierCompanyId, UUID receiverCompanyId);
}
