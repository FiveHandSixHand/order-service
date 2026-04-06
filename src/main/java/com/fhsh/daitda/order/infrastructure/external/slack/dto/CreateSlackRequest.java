package com.fhsh.daitda.order.infrastructure.external.slack.dto;

import java.util.UUID;

public record CreateSlackRequest(
	UUID orderId,
	UUID deliveryManagerId
) {
}
