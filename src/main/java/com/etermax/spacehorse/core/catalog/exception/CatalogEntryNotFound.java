package com.etermax.spacehorse.core.catalog.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class CatalogEntryNotFound extends ApiException {
	public CatalogEntryNotFound() {
	}

	public CatalogEntryNotFound(String message) {
		super(message);
	}

	public CatalogEntryNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	public CatalogEntryNotFound(Throwable cause) {
		super(cause);
	}

	public CatalogEntryNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
