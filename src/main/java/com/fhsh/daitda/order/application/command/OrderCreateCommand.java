package com.fhsh.daitda.order.application.command;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record OrderCreateCommand (
	UUID supplierCompanyId,
	UUID receiverCompanyId,
	LocalDateTime deadlineAt,
	String requestMessage,
	List<OrderItemCommand> orderItems
) {
}
