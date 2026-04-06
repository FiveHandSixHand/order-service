package com.fhsh.daitda.order.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.fhsh.daitda.order.domain.entity.Order;

public interface OrderRepository {
	public Order save(Order order);
	public Optional<Order> findById(UUID orderId);

	Slice<Order> findByOrdererId(UUID ordererId, Pageable pageable);

	Slice<Order> findAll(Pageable pageable);
}
