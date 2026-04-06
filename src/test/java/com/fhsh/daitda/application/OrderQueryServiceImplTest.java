package com.fhsh.daitda.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.fhsh.daitda.exception.BusinessException;
import com.fhsh.daitda.order.domain.exception.OrderErrorCode;
import com.fhsh.daitda.order.application.service.query.OrderQueryService;
import com.fhsh.daitda.order.application.service.query.OrderQueryServiceImpl;
import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.enums.OrderStatus;
import com.fhsh.daitda.order.domain.repository.OrderRepository;
import com.fhsh.daitda.order.infrastructure.infrastructure.strategy.OrderStrategy;

@ExtendWith(MockitoExtension.class)
class OrderQueryServiceImplTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderStrategy orderStrategy; // 모킹할 전략 객체

	private OrderQueryService orderQueryService;

	@BeforeEach
	void setUp() {
		// 리스트에 모킹된 전략을 넣어서 서비스 생성
		orderQueryService = new OrderQueryServiceImpl(orderRepository, List.of(orderStrategy));
	}

	@Test
	@DisplayName("권한에 맞는 전략을 찾아 주문 목록을 조회한다")
	void getOrders_success() {
		// given
		UUID userId = UUID.randomUUID();
		String authRole = "ADMIN";
		Pageable pageable = PageRequest.of(0, 10);
		UUID supplierId = UUID.randomUUID();
		UUID receiverId = UUID.randomUUID();
		OrderStatus status = OrderStatus.COMPLETED;
		LocalDateTime now = LocalDateTime.now();

		// Strategy가 특정 권한에 응답하도록 설정
		when(orderStrategy.equal(authRole)).thenReturn(true);
		Order realOrder = Order.builder()
			.supplierCompanyId(supplierId)
			.receiverCompanyId(receiverId)
			.ordererId(userId)
			.build();
		ReflectionTestUtils.setField(realOrder, "orderStatus", status);
		// 가짜 주문 데이터 생성 (Slice 객체)
		Slice<Order> mockSlice = new SliceImpl<>(List.of(realOrder), pageable, false);
		when(orderStrategy.fetch(userId, pageable)).thenReturn(mockSlice);

		// when
		Slice<Order> result = orderQueryService.getOrders(userId, authRole, pageable);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getContent())
			.hasSize(1)
			.extracting("supplierCompanyId", "orderStatus") // 확인하고 싶은 필드만 추출
			.containsExactly(
				tuple(supplierId, status) // 기대하는 값들과 일치하는지 한 번에 확인
			);
		verify(orderStrategy, times(1)).fetch(userId, pageable);
	}

	@Test
	@DisplayName("주문자와 요청자가 다르면 예외가 발생한다")
	void checkOrderer_fail_mismatch() {
		// given
		UUID orderId = UUID.randomUUID();
		String role = "COMPANY";
		UUID ownerId = UUID.randomUUID();
		UUID intruderId = UUID.randomUUID(); // 다른 ID

		Order order = Order.builder()
			.ordererId(ownerId)
			.build();

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

		// when & then
		assertThatThrownBy(() -> orderQueryService.getOrder(orderId, intruderId, role))
			.isInstanceOf(BusinessException.class)
			.hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.NOT_MATCH_ORDERER);
	}
}
