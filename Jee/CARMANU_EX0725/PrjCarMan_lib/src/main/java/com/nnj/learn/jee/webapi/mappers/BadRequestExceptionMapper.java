package com.nnj.learn.jee.webapi.mappers;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(final BadRequestException exception) {
        return  Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
