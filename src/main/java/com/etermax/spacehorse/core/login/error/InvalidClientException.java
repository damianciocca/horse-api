package com.etermax.spacehorse.core.login.error;

import com.etermax.spacehorse.core.error.SpaceHorseException;

public class InvalidClientException extends SpaceHorseException {

    public InvalidClientException(String message) {
        super(message);
    }

}
