package com.etermax.spacehorse.core.achievements.exception;

import static java.lang.String.format;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class AchievementStateUnknownException extends ApiException {

	public AchievementStateUnknownException(String state) {
		super(format("====> Unexpected error when trying to get achievement state. Actual [ %s ]", state));
	}
}
