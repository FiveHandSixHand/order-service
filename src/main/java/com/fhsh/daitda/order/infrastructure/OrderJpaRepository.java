package com.fhsh.daitda.order.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fhsh.daitda.order.domain.Order;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {
}
