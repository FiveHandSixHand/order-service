package com.fhsh.daitda.order.presentation;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.result.GetOrderDetailsResult;
import com.fhsh.daitda.order.application.result.GetOrdersResult;
import com.fhsh.daitda.order.application.result.OrderCreateResult;
import com.fhsh.daitda.order.application.service.command.OrderCommandService;
import com.fhsh.daitda.order.application.service.query.OrderQueryService;
import com.fhsh.daitda.response.CommonResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {
	private final OrderCommandService orderCommandService;
	private final OrderQueryService orderQueryService;

	@PostMapping
	public ResponseEntity<CommonResponse<OrderCreateResult>> createOrder(
		@RequestHeader("X-User-Id") UUID userId,
		@RequestBody OrderCreateCommand orderCreateCommand
	) {
		OrderCreateResult orderCreateResult = orderCommandService.createOrder(userId, orderCreateCommand);
		return ResponseEntity.ok(CommonResponse.success(orderCreateResult));
	}

	@GetMapping
	public ResponseEntity<CommonResponse<GetOrdersResult>> getOrders(
		@RequestHeader("X-User-Id") UUID userId,
		@RequestHeader("X-User-Role") String auth,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		GetOrdersResult result = orderQueryService.getOrders(userId, auth, pageable);
		return ResponseEntity.ok(CommonResponse.success(result));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<CommonResponse<GetOrderDetailsResult>> getOrder(
		@RequestHeader("X-User-Id") UUID userId,
		@RequestHeader("X-User-Role") String auth,
		@PathVariable UUID orderId
	) {
		GetOrderDetailsResult result = orderQueryService.getOrder(orderId, userId, auth);
		return ResponseEntity.ok(CommonResponse.success(result));
	}

	@PatchMapping("/{orderId}")
	public ResponseEntity<CommonResponse> cancelOrder(
		@RequestHeader("X-User-Id") UUID userId,
		@PathVariable UUID orderId
	) {
		orderCommandService.cancelOrder(userId, orderId);
		return ResponseEntity.ok(CommonResponse.success());
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<CommonResponse> deleteOrder(
		@RequestHeader("X-User-Id") UUID userId,
		@PathVariable UUID orderId
	) {
		orderCommandService.deleteOrder(userId, orderId);
		return ResponseEntity.ok(CommonResponse.success());
	}

}
