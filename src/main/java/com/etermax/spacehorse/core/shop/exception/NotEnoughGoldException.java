package com.etermax.spacehorse.core.shop.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class NotEnoughGoldException extends ApiException {

	@Override
	public String getMessage() {
		return "Insufficient gold";
	}

}
