package co.in.nnj.learn.jee.persi.ms;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("message")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageController {

    @Inject
    MessageRepository repo;

    @POST
    public Response addMessage(final JsonObject payLoad) {
        final long msgId = repo.addNew(payLoad.toString());
        if (msgId > 0) {
            return Response.ok(Json.createObjectBuilder().add("id", msgId).build()).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    public Response getMessage() {
        final Message message = repo.fetchAndMarkInWork();
        if (message != null) {
            final JsonObject jsonMessage = Json.createObjectBuilder()
                    .add("payload", message.getPayload())
                    .add("state", message.getStatus())
                    .add("id", message.getId()).build();

            return Response.ok(jsonMessage).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("${id}")
    public Response deleteMessage(@PathParam("id") final String messageId) {
        try {
            repo.delete(Long.valueOf(messageId));
            return Response.ok().build();
        } catch (final NumberFormatException e) {
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
