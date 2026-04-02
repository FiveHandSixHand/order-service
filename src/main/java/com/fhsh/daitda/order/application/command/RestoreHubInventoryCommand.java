package com.fhsh.daitda.order.application.command;

import java.util.UUID;

public record RestoreHubInventoryCommand(
	UUID hubInventoryId,
	int quantity
) {
}
