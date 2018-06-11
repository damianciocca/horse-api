package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class QuestBoardAlreadyInitializedException extends ApiException {

    public QuestBoardAlreadyInitializedException(String message) {
        super(message);
    }

}
