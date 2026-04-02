package com.fhsh.daitda.order.application.command;

import java.util.List;
import java.util.UUID;


public record OrderItemCommand(
	UUID productId,
	int quantity
) {
}
