package com.fhsh.daitda.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fhsh.daitda.exception.BusinessException;
import com.fhsh.daitda.order.application.client.CompanyClient;
import com.fhsh.daitda.order.application.client.DeliveryClient;
import com.fhsh.daitda.order.application.client.HubInventoryClient;
import com.fhsh.daitda.order.application.client.dto.CreateDeliveryClientResponse;
import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.command.OrderItemCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;
import com.fhsh.daitda.order.domain.exception.OrderErrorCode;
import com.fhsh.daitda.order.application.service.command.OrderCommandServiceImpl;
import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.repository.OrderRepository;
import com.fhsh.daitda.order.infrastructure.external.slack.SlackFeignClient;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class OrderCommandServiceImplTest {

	@InjectMocks
	private OrderCommandServiceImpl orderService;

	@Mock
	private OrderRepository orderRepository;
	@Mock
	private CompanyClient companyClient;
	@Mock
	private HubInventoryClient hubInventoryClient;
	@Mock
	private DeliveryClient deliveryClient;
	@Mock
	private SlackFeignClient slackFeignClient;

	@Nested
	public class CreateOrderTest {
		private UUID userId = UUID.randomUUID();

		@Test
		@DisplayName("모든 외부 서비스가 정상일 때 주문 생성에 성공해야 한다")
		void createOrder_Success() {
			// given
			UUID supplierId = UUID.randomUUID();
			UUID receiverId = UUID.randomUUID();
			UUID productId = UUID.randomUUID();
			UUID hubInventoryId = UUID.randomUUID();
			String productName = "테스트 상품";

			OrderCreateCommand command = createTestCommand(supplierId, receiverId, productId);

			// 1. 상품명 조회 Mock
			given(companyClient.getProductNames(eq(supplierId), anyList()))
				.willReturn(Map.of(productId, productName));

			// 2. 재고 차감 Mock (ProductId -> HubInventoryId 매핑)
			given(hubInventoryClient.decreaseHubInventory(eq(supplierId), anyList()))
				.willReturn(Map.of(productId, hubInventoryId));

			// 3. 배송 생성 Mock (정상적인 UUID 반환)
			UUID mockDeliveryId = UUID.randomUUID();
			UUID mockDeliveryManagerId = UUID.randomUUID();
			given(deliveryClient.createDelivery(any(), eq(supplierId), eq(receiverId)))
				.willReturn(new CreateDeliveryClientResponse(mockDeliveryId,mockDeliveryManagerId));

			// when
			OrderCreateResult result = orderService.createOrder(userId, command);

			// then
			assertNotNull(result);
			// 주문 저장 메서드가 호출되었는지 확인
			verify(orderRepository, times(1)).save(any(Order.class));
			// 배송 서비스가 호출되었는지 확인
			verify(deliveryClient, times(1)).createDelivery(any(), any(), any());
			// 재고 복구(restore)는 호출되지 않아야 함
			verify(hubInventoryClient, never()).restoreHubInventory(anyList());
		}

		@Test
		@DisplayName("배송 생성 실패 시 재고 복구 로직이 실행되어야 한다")
		void createOrder_ShouldRestoreInventory_WhenDeliveryFails() {
			// given
			UUID supplierId = UUID.randomUUID();
			UUID receiverId = UUID.randomUUID();
			UUID productId = UUID.randomUUID();
			OrderCreateCommand command = createTestCommand(supplierId, receiverId, productId);

			// Mock 설정: 재고 차감 성공 시 Map 반환
			Map<UUID, UUID> mockInventoryMap = Map.of(UUID.randomUUID(), UUID.randomUUID());
			given(hubInventoryClient.decreaseHubInventory(any(), any())).willReturn(mockInventoryMap);

			// Mock 설정: 배송 생성 시 FeignException 발생
			given(deliveryClient.createDelivery(any(), any(), any()))
				.willThrow(FeignException.class);

			// when & then
			// 1. assertThrows의 결과로 예외 객체를 받습니다.
			BusinessException exception = assertThrows(BusinessException.class, () -> {
				orderService.createOrder(userId, command);
			});

			// 2. 예외 내부의 에러 코드가 내가 설정한 것과 같은지 검증합니다.
			assertEquals(OrderErrorCode.DELIVERY_SERVICE_ERROR, exception.getErrorCode());

			// then: 배송 실패 시 restoreHubInventory가 반드시 호출되었는지 검증
			verify(hubInventoryClient, times(1)).restoreHubInventory(anyList());
		}

		private OrderCreateCommand createTestCommand(UUID supplierId, UUID receiverId, UUID productId) {
			return new OrderCreateCommand(
				supplierId,
				receiverId,
				null, // address 등 필요한 필드
				null,
				List.of(new OrderItemCommand(productId, 2))
			);
		}
	}
	
	@Nested
	class DeleteOrderTest {
		@Test
		@DisplayName("주문 삭제 성공 - 주문이 존재하면 삭제 로직이 수행되어야 한다")
		void deleteOrder_Success() {
			// given
			UUID userId = UUID.randomUUID();
			UUID orderId = UUID.randomUUID();

			// Mock 객체 생성 (실제 엔티티 기능을 테스트하기 위해 spy를 쓰거나 Mock을 설정)
			Order mockOrder = mock(Order.class);
			given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrder));

			// when
			orderService.deleteOrder(userId, orderId);

			// then
			// 1. 레포지토리에서 조회를 시도했는지 확인
			verify(orderRepository, times(1)).findById(orderId);

			// 2. 엔티티의 deleteOrder 메서드가 해당 userId로 호출되었는지 확인
			verify(mockOrder, times(1)).deleteOrder(userId);
		}
	}
}