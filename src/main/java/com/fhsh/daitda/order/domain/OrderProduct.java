package com.fhsh.daitda.order.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
record OrderProduct(
	@Column(name = "product_id_snapshot", nullable = false)
	UUID productId,
	@Column(name = "product_name_snapshot", nullable = false)
	String productName,
	@Column(nullable = false)
	Integer quantity
) {
}
