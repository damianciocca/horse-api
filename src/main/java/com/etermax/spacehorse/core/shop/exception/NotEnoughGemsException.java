package com.etermax.spacehorse.core.shop.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class NotEnoughGemsException extends ApiException {

	@Override
	public String getMessage() {
		return "Insufficient gems";
	}

}
