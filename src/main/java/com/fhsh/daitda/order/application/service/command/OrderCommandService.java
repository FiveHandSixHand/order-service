package com.fhsh.daitda.order.application.service.command;

import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;

public interface OrderCommandService {
	OrderCreateResult createOrder(OrderCreateCommand orderCreateCommand);
}
