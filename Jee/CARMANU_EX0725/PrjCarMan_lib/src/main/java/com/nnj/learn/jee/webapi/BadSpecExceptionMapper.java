package com.nnj.learn.jee.webapi;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import com.nnj.learn.jee.control.BadSpecification;

@Provider
public class BadSpecExceptionMapper implements ExceptionMapper<BadSpecification> {
    @Override
    public Response toResponse(final BadSpecification exception) {
        return  Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
