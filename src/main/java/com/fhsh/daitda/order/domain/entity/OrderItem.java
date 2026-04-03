package com.fhsh.daitda.order.domain.entity;

import java.util.UUID;

import com.fhsh.daitda.domain.BaseUserEntity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
public class OrderItem extends BaseUserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID orderItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@Getter
	private UUID hubInventoryId;

	@Embedded
	private OrderProduct orderProduct;

	static OrderItem create(UUID hubInventoryId, OrderProduct product) {
		OrderItem item = new OrderItem();
		item.hubInventoryId = hubInventoryId;
		item.orderProduct = product;
		return item;
	}

	protected void setOrder(Order order) {
		this.order = order;
	}

	public int getQuantity() {
		return this.orderProduct.quantity();
	}
}
