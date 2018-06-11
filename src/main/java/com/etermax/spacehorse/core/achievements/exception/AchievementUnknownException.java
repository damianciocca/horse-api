package com.etermax.spacehorse.core.achievements.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class AchievementUnknownException extends ApiException {

    public AchievementUnknownException(String message) {
        super(message);
    }

}
