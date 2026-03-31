package com.fhsh.daitda.order.application.command;

import java.util.UUID;

public record OrderItemCommand(
	UUID productId,
	String productName,
	Integer quantity
) {}
