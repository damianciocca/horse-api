package com.etermax.spacehorse.core.catalog.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class UndefinedCatalogException extends ApiException {

	public UndefinedCatalogException() {
	}

	public UndefinedCatalogException(String message) {
		super(message);
	}

	public UndefinedCatalogException(String message, Throwable cause) {
		super(message, cause);
	}

	public UndefinedCatalogException(Throwable cause) {
		super(cause);
	}

	public UndefinedCatalogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
