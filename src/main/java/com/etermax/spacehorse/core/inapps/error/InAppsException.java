package com.etermax.spacehorse.core.inapps.error;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class InAppsException extends ApiException {

    private final String message;
    private final Integer code;

    public InAppsException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

}
