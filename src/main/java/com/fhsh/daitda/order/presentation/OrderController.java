package com.fhsh.daitda.order.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;
import com.fhsh.daitda.order.application.service.command.OrderCommandService;
import com.fhsh.daitda.response.CommonResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController("/api/v1/orders")
public class OrderController {
	private final OrderCommandService orderCommandService;

	@PostMapping
	public ResponseEntity<CommonResponse> createOrder(@RequestBody OrderCreateCommand orderCreateCommand) {
		OrderCreateResult orderCreateResult = orderCommandService.createOrder(orderCreateCommand);
		return ResponseEntity.ok(CommonResponse.success(orderCreateResult));
	}

}
