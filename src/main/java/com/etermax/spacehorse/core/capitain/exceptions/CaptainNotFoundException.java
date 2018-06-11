package com.etermax.spacehorse.core.capitain.exceptions;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class CaptainNotFoundException extends ApiException {

	public CaptainNotFoundException(String message) {
		super(message);
	}

}
