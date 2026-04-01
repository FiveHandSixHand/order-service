package com.fhsh.daitda.order.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fhsh.daitda.order.domain.vo.OrderItemInfo;
import com.fhsh.daitda.order.domain.enums.OrderStatus;
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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID orderId;
	private UUID hubId;
	private UUID supplierCompanyId;
	private UUID receiverCompanyId;
	@Column(name = "user_id")
	private UUID ordererId;
	private UUID deliveryId;

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
	public static Order create(List<OrderItemInfo> itemInfos) {
		Order order = new Order();

		order.orderStatus = OrderStatus.CREATED;

		for (OrderItemInfo info : itemInfos) {
			OrderItem item = OrderItem.create(
				new OrderProduct(info.productId(), info.productName(), info.quantity())
			);
			order.addOrderItem(item);
		}
		return order;
	}
}
