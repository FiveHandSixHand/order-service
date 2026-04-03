package com.fhsh.daitda.order.domain.vo;

import java.util.UUID;

public record OrderItemInfo(
	UUID productId,
	String productName,
	int quantity
) {}
