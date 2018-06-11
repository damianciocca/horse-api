package com.etermax.spacehorse.core.battle.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class BattleNotFinishedException extends ApiException {

	public BattleNotFinishedException() {
	}

	public BattleNotFinishedException(String message) {
		super(message);
	}

	public BattleNotFinishedException(String message, Throwable cause) {
		super(message, cause);
	}

	public BattleNotFinishedException(Throwable cause) {
		super(cause);
	}

	public BattleNotFinishedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
