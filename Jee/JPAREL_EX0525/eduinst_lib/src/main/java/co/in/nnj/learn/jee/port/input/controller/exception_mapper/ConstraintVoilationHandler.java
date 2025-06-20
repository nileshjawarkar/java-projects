package co.in.nnj.learn.jee.port.input.controller.exception_mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import co.in.nnj.learn.jee.common.exception.ConstraintVoilationException;


@Provider
public class ConstraintVoilationHandler implements ExceptionMapper<ConstraintVoilationException>{
    @Override
    public Response toResponse(final ConstraintVoilationException e) {
        return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}
