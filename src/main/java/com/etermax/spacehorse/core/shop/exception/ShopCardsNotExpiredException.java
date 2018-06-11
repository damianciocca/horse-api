package com.etermax.spacehorse.core.shop.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class ShopCardsNotExpiredException extends ApiException {
	public ShopCardsNotExpiredException(String message) {
		super(message);
	}
}
