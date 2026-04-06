package com.fhsh.daitda.order.application.service.query;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.fhsh.daitda.order.application.result.GetOrderDetailsResult;
import com.fhsh.daitda.order.application.result.GetOrderInternalResult;
import com.fhsh.daitda.order.domain.entity.Order;

public interface OrderQueryService {
	Slice<Order> getOrders(UUID userId, String auth, Pageable pageable);

	GetOrderDetailsResult getOrder(UUID orderId, UUID userId, String authRole);
	GetOrderInternalResult getOrder(UUID orderId);
}
