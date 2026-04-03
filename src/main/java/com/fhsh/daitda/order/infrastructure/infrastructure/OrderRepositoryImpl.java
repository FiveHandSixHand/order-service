package com.fhsh.daitda.order.infrastructure.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.repository.OrderRepository;
import com.fhsh.daitda.order.infrastructure.infrastructure.repository.OrderJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {
	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Order save(Order order) {
		return orderJpaRepository.save(order);
	}

	@Override
	public Optional<Order> findById(UUID orderId) {
		return orderJpaRepository.findById(orderId);
	}

	@Override
	public Slice<Order> findByOrdererId(UUID ordererId, Pageable pageable) {
		return orderJpaRepository.findByOrdererId(ordererId, pageable);
	}

	@Override
	public Slice<Order> findAll(Pageable pageable) {
		return orderJpaRepository.findAll(pageable);
	}
}
