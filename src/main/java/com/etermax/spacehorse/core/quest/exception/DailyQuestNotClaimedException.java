package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class DailyQuestNotClaimedException extends ApiException {

	public DailyQuestNotClaimedException() {
		super("Need claim a daily quest before refresh");
	}
}
