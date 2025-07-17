package com.nnj.learn.javaee8.control;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import com.nnj.learn.javaee8.entity.InvalidEngine;

@Provider
public class InvalidEngineExceptionMapper implements ExceptionMapper<InvalidEngine>{

	@Override
	public Response toResponse(final InvalidEngine exception) {
		return Response.status(Response.Status.BAD_REQUEST)
				.header("X-car-error", exception.getMessage())
				.entity(Json.createObjectBuilder()
						.add("error_message", exception.getMessage())
						.add("status_code", Response.Status.BAD_REQUEST.getStatusCode())
						.build())
				.build();
	}
}
