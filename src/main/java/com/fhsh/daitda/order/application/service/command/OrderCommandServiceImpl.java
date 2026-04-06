package com.fhsh.daitda.order.application.service.command;

import static com.fhsh.daitda.order.domain.exception.OrderErrorCode.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhsh.daitda.exception.BusinessException;
import com.fhsh.daitda.order.application.client.CompanyClient;
import com.fhsh.daitda.order.application.client.DeliveryClient;
import com.fhsh.daitda.order.application.client.HubInventoryClient;
import com.fhsh.daitda.order.application.client.dto.CreateDeliveryClientResponse;
import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.command.OrderItemCommand;
import com.fhsh.daitda.order.application.command.RestoreHubInventoryCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;
import com.fhsh.daitda.order.domain.exception.OrderErrorCode;
import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.repository.OrderRepository;
import com.fhsh.daitda.order.domain.vo.OrderItemInfo;
import com.fhsh.daitda.order.infrastructure.external.slack.SlackFeignClient;
import com.fhsh.daitda.order.infrastructure.external.slack.dto.CreateSlackRequest;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderCommandServiceImpl implements OrderCommandService {
	private final OrderRepository orderRepository;

	private final CompanyClient companyClient;
	private final HubInventoryClient hubInventoryClient;
	private final DeliveryClient deliveryClient;
	private final SlackFeignClient slackFeignClient; //todo: 의존도 낮춰야 함

	@Transactional
	public OrderCreateResult createOrder(UUID userId, OrderCreateCommand orderCreateCommand) {
		List<UUID> productIds = orderCreateCommand.orderItems().stream()
			.map(OrderItemCommand::productId)
			.toList();
		List<OrderItemInfo> itemInfos = createOrderItemInfo(orderCreateCommand.orderItems(), productIds,
			orderCreateCommand.supplierCompanyId());

		UUID supplierCompanyId = orderCreateCommand.supplierCompanyId();
		UUID receiverCompanyId = orderCreateCommand.receiverCompanyId();

		Map<UUID, UUID> inventoryInfos = hubInventoryClient.decreaseHubInventory(supplierCompanyId,
			orderCreateCommand.orderItems());
		Order order = Order.create(supplierCompanyId,
			receiverCompanyId,
			userId,
			orderCreateCommand.deadlineAt(),
			orderCreateCommand.requestMessage(),
			itemInfos,
			inventoryInfos);

		orderRepository.save(order);

		CreateDeliveryClientResponse deliveryInfo = null;
		try {
			deliveryInfo = deliveryClient.createDelivery(order.getOrderId(), supplierCompanyId, receiverCompanyId);
		} catch (FeignException e) {
			restoreInventory(order);
			throw new BusinessException(OrderErrorCode.DELIVERY_SERVICE_ERROR);
		}
		order.complete(deliveryInfo.deliveryId());
		slackFeignClient.createSlack(new CreateSlackRequest(order.getOrderId(), deliveryInfo.deliveryManagerId())); //todo: error처리

		return OrderCreateResult.from(order);
	}

	private void restoreInventory(Order order) {
		List<RestoreHubInventoryCommand> hubInventoryCommands = order.getOrderItems()
			.stream()
			.map(item -> new RestoreHubInventoryCommand(item.getHubInventoryId(), item.getQuantity()))
			.toList();
		hubInventoryClient.restoreHubInventory(hubInventoryCommands); //todo: exception필요
	}

	private List<OrderItemInfo> createOrderItemInfo(List<OrderItemCommand> orderItemCommands, List<UUID> productIds,
		UUID supplierCompanyId) {
		Map<UUID, String> productNames = companyClient.getProductNames(supplierCompanyId, productIds);

		return orderItemCommands.stream().map(
			itemCommand -> {
				String productName = productNames.get(itemCommand.productId());
				return new OrderItemInfo(itemCommand.productId(), productName, itemCommand.quantity());
			}
		).toList();
	}

	@Transactional
	public void deleteOrder(UUID userId, UUID orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(
			() -> new BusinessException(NOT_FOUND_ORDER)
		);
		order.deleteOrder(userId);
	}

	@Transactional
	@Override
	public void cancelOrder(UUID userId, UUID orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BusinessException(NOT_FOUND_ORDER));

		order.checkOrderer(userId);
		if (order.checkStatus()) { // todo: 배송 시작 여부 함께 물어봐야 함 -> 질문: 배송 시작 전이라 받았는데 취소 처리되는 동안 상태 바꾸면??
			order.cancel();
			restoreInventory(order);
			//todo: deliveryClient 복구
		}
	}
}
