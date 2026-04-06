package com.fhsh.daitda.order.application.result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fhsh.daitda.order.domain.vo.OrderItemInfo;

public record GetOrderInternalResult(
	UUID orderId,
	UUID orderer, // todo: 이후 name으로 변경
	LocalDateTime orderAt,
	List<OrderItemInfo> infos,
	String requestMessage,
	UUID deliveryId
) {}
