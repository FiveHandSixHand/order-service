package com.fhsh.daitda.order.application.service.command;

import java.util.UUID;

import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;

public interface OrderCommandService {
	OrderCreateResult createOrder(OrderCreateCommand orderCreateCommand);
	void deleteOrder(UUID userId, UUID orderId);
}
