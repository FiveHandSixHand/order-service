package com.fhsh.daitda.order.application.result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fhsh.daitda.order.domain.enums.OrderStatus;
import com.fhsh.daitda.order.domain.vo.OrderItemInfo;

public record GetOrderDetailsResult(
	UUID supplierCompanyId, // todo: 이후 name으로 변경
	UUID receiverCompanyId,
	UUID orderer, // todo: 이후 name으로 변경
	OrderStatus status,
	LocalDateTime orderDate,
	List<OrderItemInfo> itemResult
) {}
