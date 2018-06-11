package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class QuestRefreshTimeNotReachedException extends ApiException {

	public QuestRefreshTimeNotReachedException() {
		super("Quest refresh time not reached. Unable to refresh.");
	}
}
