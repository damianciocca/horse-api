package com.etermax.spacehorse.core.login.error.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.etermax.spacehorse.core.common.resource.response.ErrorResponse;
import com.etermax.spacehorse.core.login.error.InvalidCredentialsException;

public class InvalidCredentialsExceptionHandler implements ExceptionMapper<InvalidCredentialsException> {

	@Override
	public Response toResponse(InvalidCredentialsException exception) {
		return Response.status(Response.Status.FORBIDDEN).entity(ErrorResponse.build(exception.getMessage())).build();
	}
}
