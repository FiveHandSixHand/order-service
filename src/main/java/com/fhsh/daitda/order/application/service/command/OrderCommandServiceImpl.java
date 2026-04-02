package com.fhsh.daitda.order.application.service.command;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fhsh.daitda.order.application.client.CompanyClient;
import com.fhsh.daitda.order.application.client.DeliveryClient;
import com.fhsh.daitda.order.application.client.HubInventoryClient;
import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.command.OrderItemCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;
import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.vo.OrderItemInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderCommandServiceImpl implements OrderCommandService {
	private final CompanyClient companyClient;
	private final HubInventoryClient hubInventoryClient;
	private final DeliveryClient deliveryClient;

	public OrderCreateResult createOrder(OrderCreateCommand orderCreateCommand) {
		List<UUID> productIds = orderCreateCommand.orderItems().stream()
			.map(OrderItemCommand::productId)
			.toList();
		List<OrderItemInfo> itemInfos = createOrderItemInfo(orderCreateCommand.orderItems(), productIds, orderCreateCommand.supplierCompanyId());

		UUID supplierCompanyId = orderCreateCommand.supplierCompanyId();
		UUID receiverCompanyId = orderCreateCommand.receiverCompanyId();

		Map<UUID, UUID> inventoryInfos = hubInventoryClient.decreaseHubInventory(supplierCompanyId, orderCreateCommand.orderItems());
		Order order = Order.create(itemInfos, inventoryInfos);
		//todo: 실패 시 복구 로직 필요 -> hubInventoryClient.restoreHubInventory()
		UUID deliveryId = deliveryClient.createDelivery(order.getOrderId(), supplierCompanyId, receiverCompanyId);
		order.complete(deliveryId);

		return OrderCreateResult.from(order);
	}
	private List<OrderItemInfo> createOrderItemInfo(List<OrderItemCommand> orderItemCommands, List<UUID> productIds, UUID supplierCompanyId) {
		Map<UUID, String> productNames = companyClient.getProductNames(supplierCompanyId, productIds);

		return orderItemCommands.stream().map(
			itemCommand -> {
				String productName = productNames.get(itemCommand.productId());
				return new OrderItemInfo(itemCommand.productId(), productName, itemCommand.quantity());
			}
		).toList();
	}
}
