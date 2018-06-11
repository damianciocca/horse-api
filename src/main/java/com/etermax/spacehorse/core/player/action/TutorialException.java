package com.etermax.spacehorse.core.player.action;

import com.etermax.spacehorse.core.error.SpaceHorseException;

public class TutorialException extends SpaceHorseException {
    public TutorialException(String message) {
        super(message);
    }

    public TutorialException(String message, Exception e) {
        super(message, e);
    }
}
