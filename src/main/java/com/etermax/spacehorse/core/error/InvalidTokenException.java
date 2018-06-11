package com.etermax.spacehorse.core.error;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class InvalidTokenException extends ApiException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Exception e) {
        super(message, e);
    }
}
