package com.fhsh.daitda.order.infrastructure;

import org.springframework.stereotype.Repository;

import com.fhsh.daitda.order.domain.OrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {
	private final OrderJpaRepository orderJpaRepository;
}
