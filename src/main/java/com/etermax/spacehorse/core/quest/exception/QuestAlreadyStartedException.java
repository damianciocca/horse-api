package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class QuestAlreadyStartedException extends ApiException {

	public QuestAlreadyStartedException(String message) {
		super(message);
	}
}
