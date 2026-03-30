package com.fhsh.daitda.order.domain;

import java.util.UUID;

public record OrderItemInfo(
	UUID productId,
	String productName,
	Integer quantity
) {}
