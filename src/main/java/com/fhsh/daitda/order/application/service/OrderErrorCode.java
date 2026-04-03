package com.fhsh.daitda.order.application.service;

import org.springframework.http.HttpStatus;

import com.fhsh.daitda.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements ErrorCode {
	//external error
	DELIVERY_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DELIVERY_SERVICE_ERROR"),

	//NOT FOUND
	NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "NOT_FOUND_ORDER");
	private final HttpStatus status;
	private final String description;
}
