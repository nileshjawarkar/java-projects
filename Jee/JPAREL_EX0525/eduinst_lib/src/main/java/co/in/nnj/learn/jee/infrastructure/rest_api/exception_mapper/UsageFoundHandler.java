package co.in.nnj.learn.jee.infrastructure.rest_api.exception_mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import co.in.nnj.learn.jee.application.exception.UsageFound;

@Provider
public class UsageFoundHandler implements ExceptionMapper<UsageFound> {
    @Override
    public Response toResponse(final UsageFound exception) {
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(String.format("Error - Usage found. %s", exception.getMessage())).build();
    }
}
