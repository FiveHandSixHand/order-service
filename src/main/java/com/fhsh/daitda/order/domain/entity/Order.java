package com.fhsh.daitda.order.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fhsh.daitda.domain.BaseUserEntity;
import com.fhsh.daitda.order.domain.enums.OrderStatus;
import com.fhsh.daitda.order.domain.vo.OrderItemInfo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseUserEntity {
	@Getter
	@GeneratedValue(strategy = GenerationType.UUID)
	@Id
	private UUID orderId;
	private UUID supplierCompanyId;
	private UUID receiverCompanyId;
	@Column(name = "user_id")
	private UUID ordererId;
	private UUID deliveryId;

	@Getter
	@OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	@Embedded
	private OrderRequest orderRequest;

	public void addOrderItem(OrderItem item) {
		orderItems.add(item);
		item.setOrder(this);  // 양쪽 동시 세팅
	}

	// Order 와 OrderItem 저장 확인 위해 간단히, 추후 수정 예정
	public static Order create(List<OrderItemInfo> itemInfos, Map<UUID, UUID> hubInventoryInfos) {
		Order order = new Order();

		order.orderStatus = OrderStatus.CREATED;

		for (OrderItemInfo info : itemInfos) {
			OrderItem item = OrderItem.create(
				hubInventoryInfos.get(info.productId()),
				new OrderProduct(info.productId(), info.productName(), info.quantity())
			);
			order.addOrderItem(item);
		}
		return order;
	}

	public void complete(UUID deliveryId) {
		this.deliveryId = deliveryId;
		this.orderStatus = OrderStatus.COMPLETED;
	}
}
