package com.fhsh.daitda.order.infrastructure.external;

import java.util.UUID;

public class DeliveryRequestDto {
	public record Creation(
		UUID orderId,
		UUID supplierCompanyId,
		UUID receiverCompanyId
	) {}
}
