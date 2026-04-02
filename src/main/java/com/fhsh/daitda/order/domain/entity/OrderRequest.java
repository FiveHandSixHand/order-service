package com.fhsh.daitda.order.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;

@Embeddable
record OrderRequest(
	LocalDateTime deadlineAt,
	String requestMessage
) {
}
