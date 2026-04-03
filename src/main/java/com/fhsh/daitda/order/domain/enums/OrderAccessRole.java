package com.fhsh.daitda.order.domain.enums;

import java.util.Arrays;

public enum OrderAccessRole {
	ADMIN,
	HUB_ADMIN,
	DELIVERY,
	COMPANY;

	public static OrderAccessRole from(String auth) {
		return Arrays.stream(values())
			.filter(role -> role.name().equals(auth))
			.findFirst()
			.orElseThrow();
	}
}
