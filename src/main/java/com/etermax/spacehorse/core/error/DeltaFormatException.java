package com.etermax.spacehorse.core.error;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class DeltaFormatException extends ApiException {
	public DeltaFormatException() {
	}

	public DeltaFormatException(String message) {
		super(message);
	}
}
