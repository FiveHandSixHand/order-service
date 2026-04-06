package com.fhsh.daitda.order.domain.enums;

import com.fhsh.daitda.exception.BusinessException;
import com.fhsh.daitda.order.domain.exception.OrderErrorCode;

public enum OrderStatus {
	CREATED {
		@Override
		public boolean validateCanCancel() {
			return true;
		}
	},
	COMPLETED {
		@Override
		public boolean validateCanCancel() {
			return true;
		}
	},
	FAILED {
		@Override
		public boolean validateCanCancel() {
			throw new BusinessException(OrderErrorCode.ALREADY_FAILED_ORDER);
		}
	},
	CANCELLED {
		@Override
		public boolean validateCanCancel() {
			throw new BusinessException(OrderErrorCode.ALREADY_CANCELLED);
		}
	};

	public abstract boolean validateCanCancel();
}