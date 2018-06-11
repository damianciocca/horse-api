package com.etermax.spacehorse.core.error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ClientResponseException extends WebApplicationException{
    private Throwable cause;

    public ClientResponseException(Throwable cause, int errorCode) {
        super(Response.status(errorCode).entity(cause.getMessage()).type(MediaType.TEXT_PLAIN).build());
        this.cause = cause;
    }

    @Override
    public Throwable getCause() { return cause; }

    @Override
    public String getMessage() {
        return cause.getMessage();
    }
}
