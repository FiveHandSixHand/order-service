package com.fhsh.daitda.order.application.client;

import java.util.UUID;

public interface DeliveryClient {
	UUID createDelivery(UUID orderId, UUID supplierCompanyId, UUID receiverCompanyId);
}
