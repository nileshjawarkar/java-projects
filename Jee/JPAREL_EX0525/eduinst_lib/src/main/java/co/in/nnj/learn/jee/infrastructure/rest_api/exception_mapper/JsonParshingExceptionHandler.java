package co.in.nnj.learn.jee.infrastructure.rest_api.exception_mapper;

import jakarta.json.stream.JsonParsingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class JsonParshingExceptionHandler implements ExceptionMapper<JsonParsingException> {
    @Override
    public Response toResponse(final JsonParsingException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(String.format("Invalid input. %s", exception.getMessage())).build();
    }
}
