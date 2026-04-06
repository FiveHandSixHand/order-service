package com.fhsh.daitda.order.application.service.query;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhsh.daitda.exception.BusinessException;
import com.fhsh.daitda.order.application.result.GetOrderDetailsResult;
import com.fhsh.daitda.order.application.result.GetOrderInternalResult;
import com.fhsh.daitda.order.application.result.GetOrdersResult;
import com.fhsh.daitda.order.domain.exception.OrderErrorCode;
import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.enums.OrderAccessRole;
import com.fhsh.daitda.order.domain.repository.OrderRepository;
import com.fhsh.daitda.order.domain.vo.OrderItemInfo;
import com.fhsh.daitda.order.infrastructure.infrastructure.strategy.OrderStrategy;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderQueryServiceImpl implements OrderQueryService{
	private final OrderRepository orderRepository;
	// (1) Spring이 컨테이너를 뒤져서 OrderStrategy 타입의 모든 빈을 찾고
	// (2) 자동으로 List 객체를 만들어 주입
	private final List<OrderStrategy> orderStrategies;
	@Override
	public Slice<Order> getOrders(UUID userId, String authRole, Pageable pageable) {

		OrderStrategy strategy = orderStrategies.stream()
			.filter(s -> s.equal(authRole))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 권한입니다."));

		Slice<Order> orders = strategy.fetch(userId, pageable);

		return orders;
	}

	@Override
	public GetOrderDetailsResult getOrder(UUID orderId, UUID userId, String authRole) {

		Order order = orderRepository.findById(orderId).orElseThrow(
			() -> new BusinessException(OrderErrorCode.NOT_FOUND_ORDER)
		);
		OrderAccessRole role = OrderAccessRole.from(authRole);
		// todo: 이후 정책 달라질 수 있으니 refactoring 필요
		if (role.equals(OrderAccessRole.COMPANY) || role.equals(OrderAccessRole.DELIVERY)) {
			order.checkOrderer(userId);
		}
		List<OrderItemInfo> itemResults = order.getOrderItemInfo();

		return new GetOrderDetailsResult(
			order.getSupplierCompanyId(),
			order.getReceiverCompanyId(),
			order.getOrdererId(),
			order.getOrderStatus(),
			order.getCreatedAt(),
			itemResults
		);
	}

	//internal
	@Override
	public GetOrderInternalResult getOrder(UUID orderId) {

		Order order = orderRepository.findById(orderId).orElseThrow(
			() -> new BusinessException(OrderErrorCode.NOT_FOUND_ORDER)
		);

		return new GetOrderInternalResult(
			order.getOrderId(),
			order.getOrdererId(),
			order.getCreatedAt(),
			order.getOrderItemInfo(),
			order.getRequestMsg(),
			order.getDeliveryId()
		);
	}
}
