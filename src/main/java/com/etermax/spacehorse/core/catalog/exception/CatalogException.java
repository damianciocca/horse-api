package com.etermax.spacehorse.core.catalog.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class CatalogException extends ApiException {

	public CatalogException() {
		super();
	}

	public CatalogException(String message) {
		super(message);
	}

	public CatalogException(String message, Throwable cause) {
		super(message, cause);
	}

	public CatalogException(Throwable cause) {
		super(cause);
	}

	protected CatalogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
