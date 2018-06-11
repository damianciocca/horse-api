package com.etermax.spacehorse.core.achievements.exception;

import static java.lang.String.format;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class AchievementInvalidStateException extends ApiException {

	public AchievementInvalidStateException(String state) {
		super(format("====> Invalid achievement state. Actual [ %s ]", state));
	}
}
