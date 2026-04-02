package com.fhsh.daitda.order.application.client;

import java.util.UUID;

public interface HubInventoryClient {
	boolean decreaseHubInventory(UUID supplierCompanyId, UUID productId, int quantity);
	boolean restoreHubInventory(UUID hubInventoryId, int quantity);
}
