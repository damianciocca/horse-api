package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class QuestIncompleteException extends ApiException {

    public QuestIncompleteException(String message) {
        super(message);
    }

}
