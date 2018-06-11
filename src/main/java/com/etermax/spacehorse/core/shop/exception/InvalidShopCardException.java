package com.etermax.spacehorse.core.shop.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class InvalidShopCardException extends ApiException {
	public InvalidShopCardException(String message) {
		super(message);
	}
}
