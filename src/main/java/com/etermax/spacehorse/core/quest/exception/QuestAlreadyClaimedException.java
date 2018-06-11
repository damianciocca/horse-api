package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class QuestAlreadyClaimedException  extends ApiException {

    public QuestAlreadyClaimedException(String message) {
        super(message);
    }

}