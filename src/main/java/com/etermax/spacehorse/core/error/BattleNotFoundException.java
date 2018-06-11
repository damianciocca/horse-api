package com.etermax.spacehorse.core.error;

public class BattleNotFoundException extends SpaceHorseException {
    public BattleNotFoundException(String message) {
        super(message);
    }

    public BattleNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
