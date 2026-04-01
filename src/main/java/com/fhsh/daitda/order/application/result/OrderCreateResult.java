package com.fhsh.daitda.order.application.result;

import java.util.UUID;

import com.fhsh.daitda.order.domain.entity.Order;

public record OrderCreateResult(
	UUID orderId
) {
	public static OrderCreateResult from(Order order) {
		return new OrderCreateResult(
			order.getOrderId()
		);
	}
}
