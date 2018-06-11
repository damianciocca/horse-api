package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class QuestBoardSkipTimeNotReachedException extends ApiException {

    public QuestBoardSkipTimeNotReachedException() {
        super("Quest board skip time not reached. Unable to skip.");
    }

}
