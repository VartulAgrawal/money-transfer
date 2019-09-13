package com.revolut.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class InsufficientFundsExceptionResponse implements ExceptionMapper<InsufficientFundsException> {

	@Override
	public Response toResponse(InsufficientFundsException e) {
		return Response.status(Status.NOT_ACCEPTABLE)
				.entity(new CommonError(e.getMessage()))
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
	}
}
