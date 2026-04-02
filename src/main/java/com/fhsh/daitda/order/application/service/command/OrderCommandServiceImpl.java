package com.fhsh.daitda.order.application.service.command;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fhsh.daitda.exception.BusinessException;
import com.fhsh.daitda.order.application.client.CompanyClient;
import com.fhsh.daitda.order.application.client.DeliveryClient;
import com.fhsh.daitda.order.application.client.HubInventoryClient;
import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.command.OrderItemCommand;
import com.fhsh.daitda.order.application.command.RestoreHubInventoryCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;
import com.fhsh.daitda.order.application.service.OrderErrorCode;
import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.repository.OrderRepository;
import com.fhsh.daitda.order.domain.vo.OrderItemInfo;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderCommandServiceImpl implements OrderCommandService {
	private final OrderRepository orderRepository;

	private final CompanyClient companyClient;
	private final HubInventoryClient hubInventoryClient;
	private final DeliveryClient deliveryClient;

	public OrderCreateResult createOrder(OrderCreateCommand orderCreateCommand) {
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
			orderCreateCommand.deadlineAt(),
			orderCreateCommand.requestMessage(),
			itemInfos,
			inventoryInfos);

		UUID deliveryId = null;
		try {
			deliveryId = deliveryClient.createDelivery(order.getOrderId(), supplierCompanyId, receiverCompanyId);
		} catch (FeignException e) {
			List<RestoreHubInventoryCommand> hubInventoryCommands = order.getOrderItems()
				.stream()
				.map(item -> new RestoreHubInventoryCommand(item.getHubInventoryId(), item.getQuantity()))
				.toList();
			hubInventoryClient.restoreHubInventory(hubInventoryCommands);
			throw new BusinessException(OrderErrorCode.DELIVERY_SERVICE_ERROR);
		}
		order.complete(deliveryId);

		orderRepository.save(order);

		return OrderCreateResult.from(order);
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
}
