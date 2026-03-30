package com.fhsh.daitda.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.fhsh.daitda.order.domain.Order;
import com.fhsh.daitda.order.domain.OrderItemInfo;
import com.fhsh.daitda.order.domain.OrderRepository;
import com.fhsh.daitda.order.infrastructure.OrderRepositoryImpl;

@DataJpaTest
@Import(OrderRepositoryImpl.class)
@ActiveProfiles("test")
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@Test
	@DisplayName("Record 기반 OrderProduct를 포함한 주문 저장 및 조회 테스트")
	public void dbTest() {
		List<OrderItemInfo> orderItemInfos = List.of(
			new OrderItemInfo(UUID.randomUUID(),  "오징어", 50),
			new OrderItemInfo(UUID.randomUUID(),  "갈치", 26)
		);

		Order order = Order.create(orderItemInfos);

		Order savedOrder = orderRepository.save(order);
		UUID orderId = (UUID) ReflectionTestUtils.getField(savedOrder, "orderId");
		Order result = orderRepository.findById(orderId).get();

		assertThat(result)
			.extracting("orderItems")
			.asList()
			.hasSize(2)
			.extracting("orderProduct.productName")
			.containsExactlyInAnyOrder("오징어", "갈치");
	}
}
