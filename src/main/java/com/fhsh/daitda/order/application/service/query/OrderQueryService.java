package com.fhsh.daitda.order.application.service.query;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.fhsh.daitda.order.application.result.GetOrdersResult;

public interface OrderQueryService {
	GetOrdersResult getOrders(UUID userId, String auth, Pageable pageable);
}
