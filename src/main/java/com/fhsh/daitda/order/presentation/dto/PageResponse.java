package com.fhsh.daitda.order.presentation.dto;

import java.util.List;

public class PageResponse<T> {
	private boolean hasNext;
	private List<T> data;

	public PageResponse(boolean hasNext, List<T> data) {
		this.hasNext = hasNext;
		this.data = data;
	}

}
