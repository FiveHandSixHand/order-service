package com.fhsh.daitda.order.presentation;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fhsh.daitda.order.application.result.GetOrderDetailsResult;
import com.fhsh.daitda.order.application.result.GetOrderInternalResult;
import com.fhsh.daitda.order.application.service.query.OrderQueryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/internal/v1/orders")
@RestController
public class OrderInternalController {
	private final OrderQueryService orderQueryService;
	@GetMapping("/{orderId}")
	public GetOrderInternalResult getOrder(
		@PathVariable UUID orderId
	) {
		GetOrderInternalResult result = orderQueryService.getOrder(orderId);
		return result;
	}
}
