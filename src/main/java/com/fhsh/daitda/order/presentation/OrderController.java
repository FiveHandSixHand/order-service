package com.fhsh.daitda.order.presentation;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;
import com.fhsh.daitda.order.application.service.command.OrderCommandService;
import com.fhsh.daitda.order.application.service.query.OrderQueryService;
import com.fhsh.daitda.response.CommonResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {
	private UUID userId; // 이후 삭제
	private final OrderCommandService orderCommandService;
	private final OrderQueryService orderQueryService;

	@PostMapping
	public ResponseEntity<CommonResponse> createOrder(@RequestBody OrderCreateCommand orderCreateCommand) {
		OrderCreateResult orderCreateResult = orderCommandService.createOrder(orderCreateCommand);
		return ResponseEntity.ok(CommonResponse.success(orderCreateResult));
	}

	@GetMapping
	public ResponseEntity<CommonResponse> getOrders(
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) @RequestBody Pageable pageable) {
		String auth=""; //바뀔 예정
		orderQueryService.getOrders(userId, auth, pageable);
		return ResponseEntity.ok(CommonResponse.success());
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<CommonResponse> deleteOrder(@PathVariable UUID orderId) {
		orderCommandService.deleteOrder(userId, orderId);
		return ResponseEntity.ok(CommonResponse.success());
	}

}
