package com.etermax.spacehorse.core.error;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class SpaceHorseException extends ApiException {
    public SpaceHorseException(String message) { super(message); }

    public SpaceHorseException(String message, Exception e) { super(message, e); }
}
