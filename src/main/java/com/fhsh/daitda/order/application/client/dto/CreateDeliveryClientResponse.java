package com.fhsh.daitda.order.application.client.dto;

import java.util.UUID;

public record CreateDeliveryClientResponse(
	UUID deliveryId,
	UUID deliveryManagerId
) {
}
