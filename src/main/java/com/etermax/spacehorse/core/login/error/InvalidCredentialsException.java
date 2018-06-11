package com.etermax.spacehorse.core.login.error;

import com.etermax.spacehorse.core.error.SpaceHorseException;

public class InvalidCredentialsException extends SpaceHorseException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Exception e) {
        super(message, e);
    }

}
