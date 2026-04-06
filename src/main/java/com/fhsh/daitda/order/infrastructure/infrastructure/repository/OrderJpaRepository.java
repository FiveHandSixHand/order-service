package com.fhsh.daitda.order.infrastructure.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fhsh.daitda.order.domain.entity.Order;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {
	Slice<Order> findByOrdererId(UUID ordererId, Pageable pageable);
}
