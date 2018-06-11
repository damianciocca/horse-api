package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class DailyQuestIsNotInitializedException extends ApiException {

	public DailyQuestIsNotInitializedException() {
		super("Daily quest need be initialized");
	}
}
