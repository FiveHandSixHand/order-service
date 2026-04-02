package com.fhsh.daitda.order.application.service.command;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhsh.daitda.order.application.client.CompanyClient;
import com.fhsh.daitda.order.application.command.OrderCreateCommand;
import com.fhsh.daitda.order.application.command.OrderItemCommand;
import com.fhsh.daitda.order.application.result.OrderCreateResult;
import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.vo.OrderItemInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderCommandServiceImpl implements OrderCommandService {
	@Autowired
	private final CompanyClient companyClient;

	public OrderCreateResult createOrder(OrderCreateCommand orderCreateCommand) {
		List<UUID> productIds = orderCreateCommand.orderItems().stream()
			.map(OrderItemCommand::productId)
			.toList();
		List<OrderItemInfo> itemInfos = createOrderItemInfo(orderCreateCommand.orderItems(), productIds, orderCreateCommand.supplierCompanyId());

		Order order = Order.create(itemInfos);

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
