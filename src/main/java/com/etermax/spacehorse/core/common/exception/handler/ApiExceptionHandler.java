package com.etermax.spacehorse.core.common.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.common.resource.response.ErrorResponse;

public class ApiExceptionHandler implements ExceptionMapper<ApiException> {

	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@Override
	public Response toResponse(ApiException exception) {
		logger.error("Api Exception", exception);
		Response response400 = Response
				.status(Response.Status.BAD_REQUEST)
				.entity(ErrorResponse.build(exception.getMessage()))
				.build();
		return response400;
	}
}
