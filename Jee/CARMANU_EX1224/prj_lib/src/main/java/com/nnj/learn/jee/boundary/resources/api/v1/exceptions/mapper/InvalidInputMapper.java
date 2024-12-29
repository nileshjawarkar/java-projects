package com.nnj.learn.jee.boundary.resources.api.v1.exceptions.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import com.nnj.learn.jee.boundary.resources.api.v1.exceptions.InvalidInputException;

@Provider
public class InvalidInputMapper implements ExceptionMapper<InvalidInputException>{

    @Override
    public Response toResponse(final InvalidInputException e) {
        return Response.status(Response.Status.BAD_REQUEST)
        .header("X-error", e.getMessage())
        .build();
    }
}
