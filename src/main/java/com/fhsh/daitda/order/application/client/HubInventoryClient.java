package com.fhsh.daitda.order.application.client;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fhsh.daitda.order.application.command.OrderItemCommand;
import com.fhsh.daitda.order.application.command.RestoreHubInventoryCommand;

public interface HubInventoryClient {
	Map<UUID, UUID> decreaseHubInventory(UUID supplierCompanyId, List<OrderItemCommand> orderItems);
	void restoreHubInventory(List<RestoreHubInventoryCommand> hubInventoryCommands);
}
