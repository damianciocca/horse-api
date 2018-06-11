package com.etermax.spacehorse.core.catalog.exception;

import com.etermax.spacehorse.core.error.SpaceHorseException;

public class NoStartingCardsDefinedInGameConstantsException extends SpaceHorseException {

    public NoStartingCardsDefinedInGameConstantsException(String message) {
        super(message);
    }

    public NoStartingCardsDefinedInGameConstantsException(String message, Exception e) {
        super(message, e);
    }
}
