package com.etermax.spacehorse.core.shop.exception.handler;

import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.common.resource.response.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class NotEnoughGemsHandler implements ExceptionMapper<ApiException> {

	@Override
	public Response toResponse(ApiException exception) {
		Response response412 = Response
				.status(Response.Status.PRECONDITION_FAILED)
				.entity(ErrorResponse.build(exception.getMessage()))
				.build();
		return response412;
	}

}