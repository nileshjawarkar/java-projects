package com.nnj.learn.jee.webapi.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import com.nnj.learn.jee.exception.BadSpecification;

@Provider
public class BadSpecificationExceptionMapper implements ExceptionMapper<BadSpecification> {
    @Override
    public Response toResponse(final BadSpecification exception) {
        return  Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
