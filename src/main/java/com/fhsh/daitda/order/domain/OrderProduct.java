package com.fhsh.daitda.order.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {
	@Column(name="product_id_snapshot")
	private UUID productId;
	@Column(name="product_name_snapshot")
	private String productName;
	private Integer quantity;

	public OrderProduct(UUID productId, String productName, Integer quantity) {
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
	}
}
