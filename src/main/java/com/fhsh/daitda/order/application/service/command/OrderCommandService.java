package com.fhsh.daitda.order.application.service.command;

import java.util.UUID;

import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;

public interface OrderCommandService {
	OrderCreateResult createOrder(UUID userId, OrderCreateCommand orderCreateCommand);
	void deleteOrder(UUID userId, UUID orderId);
	void cancelOrder(UUID userId, UUID orderId);
}
