package com.etermax.spacehorse.core.shop.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class ShopCardNotFoundException extends ApiException {
	public ShopCardNotFoundException(String message) {
		super(message);
	}
}
