package com.fhsh.daitda.order.infrastructure.infrastructure.strategy;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.fhsh.daitda.order.domain.entity.Order;

public interface OrderStrategy {
	boolean equal(String auth);
	Slice<Order> fetch(UUID userId, Pageable pageable);
}
