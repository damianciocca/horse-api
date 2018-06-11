package com.etermax.spacehorse.core.capitain.exceptions;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class UnlockingNotAllowedException extends ApiException {

	public UnlockingNotAllowedException(String message) {
		super(message);
	}

}
