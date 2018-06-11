package com.etermax.spacehorse.core.shop.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class ExpiredShopCardsException extends ApiException {
	public ExpiredShopCardsException(String message) {
		super(message);
	}
}
