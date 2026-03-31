package com.fhsh.daitda.order.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.fhsh.daitda.order.domain.entity.Order;

public interface OrderRepository {
	public Order save(Order order);
	public Optional<Order> findById(UUID orderId);
}
