package co.in.nnj.learn.jee.persi.rel;

import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    UserRepository repo;

    @POST
    public Response createUser(@Valid final User user) {
        return Response.ok(repo.createUser(user)).build();
    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") final String userId) {
        return repo.findUser(UUID.fromString(userId)).map(user -> Response.ok(user).build()).orElseThrow();
    }

    @POST
    @Path("{id}/address")
    public Response updateAddress(@PathParam("id") final String userId, final Address address) {
        if(repo.assignAddress(UUID.fromString(userId), address)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
