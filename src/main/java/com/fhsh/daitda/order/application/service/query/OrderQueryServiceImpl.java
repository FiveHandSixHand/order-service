package com.fhsh.daitda.order.application.service.query;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.fhsh.daitda.order.application.result.GetOrdersResult;
import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.repository.OrderRepository;
import com.fhsh.daitda.order.infrastructure.infrastructure.strategy.OrderStrategy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderQueryServiceImpl implements OrderQueryService{
	private final OrderRepository orderRepository;
	// (1) Spring이 컨테이너를 뒤져서 OrderStrategy 타입의 모든 빈을 찾고
	// (2) 자동으로 List 객체를 만들어 주입
	private final List<OrderStrategy> orderStrategies;
	@Override
	public GetOrdersResult getOrders(UUID userId, String auth, Pageable pageable) {

		OrderStrategy strategy = orderStrategies.stream()
			.filter(s -> s.equal(auth))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 권한입니다."));

		Slice<Order> orders = strategy.fetch(userId, pageable);
		GetOrdersResult ordersResult = new GetOrdersResult(orders.stream().map(order -> {
			return new GetOrdersResult.OrderResult(order.getSupplierCompanyId(),
				order.getReceiverCompanyId(),
				order.getOrdererId(),
				order.getOrderStatus(),
				order.getCreatedAt());
		}).toList());
		// return new GetOrderResult(orders);
		return ordersResult;
	}
}
