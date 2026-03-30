package com.fhsh.daitda.order.domain;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
	public Order save(Order order);
	public Optional<Order> findById(UUID orderId);
}
