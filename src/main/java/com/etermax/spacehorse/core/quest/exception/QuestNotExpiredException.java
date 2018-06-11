package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class QuestNotExpiredException extends ApiException {

    public QuestNotExpiredException(String message) {
        super(message);
    }

}
