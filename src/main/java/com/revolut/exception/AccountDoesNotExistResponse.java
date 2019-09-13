package com.revolut.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class AccountDoesNotExistResponse implements ExceptionMapper<AccountDoesNotExistException> {

	@Override
	public Response toResponse(AccountDoesNotExistException e) {
		return Response.status(Status.NOT_FOUND)
				.entity(new CommonError(e.getMessage()))
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
	}
}
