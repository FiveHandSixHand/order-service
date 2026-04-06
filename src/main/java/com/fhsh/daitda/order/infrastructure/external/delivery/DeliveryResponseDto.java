package com.fhsh.daitda.order.infrastructure.external.delivery;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeliveryResponseDto {
	public record CreateDelivery (
		 UUID deliveryId,
		 UUID orderId,
		 String status,
		 UUID srcHubId,
		 UUID destHubId,
		 String senderTenantAddress,
		 String receiverTenantAddress,
		 UUID receiverId,
		 UUID deliveryManagerId,
		 LocalDateTime createdAt
	) {}
}
