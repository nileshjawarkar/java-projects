package co.in.nnj.learn.jee.persi.rel;

import java.util.Optional;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartController {
    @Inject
    CartRepository repo;

    public JsonObject cartToJson(final Cart cart) {
        return Json.createObjectBuilder()
                .add("owner", cart.getOwner())
                .add("id", cart.getId().toString())
                .add("items", cart.getItems().stream().map(item -> Json.createValue(item.getId().toString()))
                        .collect(JsonCollectors.toJsonArray()))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getCart(@PathParam("id") final String id) {
        return repo.findCart(UUID.fromString(id)).map(cart -> Response.ok(cartToJson(cart)))
                .orElseGet(Response::noContent).build();
    }

    @POST
    public Response createCart(final Cart cart) {
        return Optional.ofNullable(repo.createCart(cart))
                .map(Response::ok)
                .orElseGet(Response::serverError)
                .build();
    }

    @POST
    @Path("{id}/item")
    public Response addItem(@PathParam("id") final String cartId, final Item item) {
        /*
         * return repo.findCart(UUID.fromString(cartId)).map(cart -> {
         * Set<Item> items = cart.getItems();
         * if (items == null) {
         * items = new HashSet<>();
         * }
         * item.setCart(cart);
         * items.add(item);
         * repo.updateCart(cart);
         * return Response.ok();
         * }).orElseGet(Response::noContent).build();
         */

        final Item newItem = repo.addItemToCart(UUID.fromString(cartId), item);
        return Response
                .ok(Json.createObjectBuilder().add("id",
                        newItem.getId().toString()).add("title", newItem.getTitle()).build())
                .build();
    }

    @GET
    @Path("{id}/item")
    public Response getItems(@PathParam("id") final String cartId) {
        final JsonArray itemArray = repo.getItemsInCart(UUID.fromString(cartId)).stream().map(item -> {
            return Json.createObjectBuilder()
                    .add("id", item.getId().toString())
                    .add("title", item.getTitle()).build();
        }).collect(JsonCollectors.toJsonArray());
        return Response.ok(itemArray).build();
    }
}
