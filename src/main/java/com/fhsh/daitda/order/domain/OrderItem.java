package com.fhsh.daitda.order.domain;

import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderItem {
	@Id
	private UUID orderItemId;
	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	private UUID hubInventoryId;

	@Embedded
	private OrderProduct orderProduct;

	static OrderItem create(OrderProduct product) {
		OrderItem item = new OrderItem();
		item.orderItemId = UUID.randomUUID();
		item.orderProduct = product;
		return item;
	}

	protected void setOrder(Order order) {
		this.order = order;
	}
}
