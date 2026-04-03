package com.fhsh.daitda.order.infrastructure.infrastructure.strategy;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.fhsh.daitda.order.domain.entity.Order;
import com.fhsh.daitda.order.domain.enums.OrderAccessRole;
import com.fhsh.daitda.order.domain.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserOrderStrategy implements OrderStrategy {
	private final OrderRepository orderRepository;

	@Override
	public boolean equal(String auth) {
		return OrderAccessRole.from(auth) == OrderAccessRole.COMPANY
			|| OrderAccessRole.from(auth) == OrderAccessRole.DELIVERY;
	}

	@Override
	public Slice<Order> fetch(UUID userId, Pageable pageable) {
		return orderRepository.findByOrdererId(userId, pageable);
	}
}
