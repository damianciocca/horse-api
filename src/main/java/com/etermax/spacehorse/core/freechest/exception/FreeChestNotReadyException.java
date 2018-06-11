package com.etermax.spacehorse.core.freechest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class FreeChestNotReadyException extends ApiException {

	public FreeChestNotReadyException() {
	}

	public FreeChestNotReadyException(String message) {
		super(message);
	}

	public FreeChestNotReadyException(String message, Throwable cause) {
		super(message, cause);
	}

	public FreeChestNotReadyException(Throwable cause) {
		super(cause);
	}

	public FreeChestNotReadyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
