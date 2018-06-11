package com.etermax.spacehorse.core.user.exceptions;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class RoleChangingNotAllowedException extends ApiException {

	public RoleChangingNotAllowedException(String message) {
		super(message);
	}

}
