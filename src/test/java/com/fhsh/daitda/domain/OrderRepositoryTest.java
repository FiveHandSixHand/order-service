package com.fhsh.daitda.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.vo.OrderItemInfo;
import com.fhsh.daitda.order.domain.repository.OrderRepository;
import com.fhsh.daitda.order.infrastructure.infrastructure.OrderRepositoryImpl;

@DataJpaTest
@Import(OrderRepositoryImpl.class)
@ActiveProfiles("test")
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@Test
	@DisplayName("Record 기반 OrderProduct를 포함한 주문 저장 및 조회 테스트")
	public void dbTest() {
		final UUID hubInvenId1 = UUID.randomUUID();
		final UUID hubInvenId2 = UUID.randomUUID();
		final UUID prod1 = UUID.randomUUID();
		final UUID prod2 = UUID.randomUUID();
		List<OrderItemInfo> orderItemInfos = List.of(
			new OrderItemInfo(prod1,  "오징어", 50),
			new OrderItemInfo(prod2,  "갈치", 26)
		);
		Map<UUID, UUID> inventoryMap = Map.of(
			prod1, hubInvenId1,
			prod2, hubInvenId2
		);

		Order order = Order.create(null, null, null, null, orderItemInfos, inventoryMap);

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
