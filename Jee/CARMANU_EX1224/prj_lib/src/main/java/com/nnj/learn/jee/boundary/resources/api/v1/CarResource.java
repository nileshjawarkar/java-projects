package com.nnj.learn.jee.boundary.resources.api.v1;

import java.net.URI;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import com.nnj.learn.jee.boundary.CarManufacturer;
import com.nnj.learn.jee.boundary.resources.api.v1.exceptions.InvalidInputException;
import com.nnj.learn.jee.entity.Car;
import com.nnj.learn.jee.entity.Color;
import com.nnj.learn.jee.entity.EngineType;
import com.nnj.learn.jee.entity.Specification;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarResource {

    @Context
    UriInfo uriInfo;

    @Inject
    CarManufacturer carManufacturer;

    /*
     * Uses JAX-B defaults
     * 
     * @GET
     * public List<Car> getCars() {
     * return carManufacturer.retrieveAllCars();
     * }
     */

    @GET
    public Response getCars(@QueryParam("limit") final Integer limit, @QueryParam("offset") final Integer offset) {
        final List<Car> cars = carManufacturer.retrieveAllCars(limit, offset);
        if (cars != null && cars.size() > 0) {
            /*
             * final JsonArray jsonArray = cars.stream().map(car ->
             * Json.createObjectBuilder()
             * .add("id", car.getIdentifier())
             * .add("color", car.getColor().toString())
             * .add("engine", car.getEngineType().toString())
             * .build())
             * .collect(JsonCollectors.toJsonArray());
             */

            final JsonArray jsonArray = cars.stream().map(car -> Json.createValue(car.getIdentifier()))
                    .collect(JsonCollectors.toJsonArray());
            return Response.ok().entity(jsonArray).header("X-Size", Integer.toString(cars.size())).build();
        }
        return Response.noContent().build();
    }

    /*
     * Uses JAX-B defaults
     * 
     * @POST
     * public Car addCar(final Specification spec) {
     * return carManufacturer.manufactureCar(spec);
     * }
     */

    @POST
    public Response addCar(@NotNull final JsonObject jsonObject) {
        final String strColor = jsonObject.containsKey("color") ? jsonObject.getString("color") : null;
        final String strEngine = jsonObject.containsKey("engine") ? jsonObject.getString("engine") : null;
        final EngineType engine = EngineType.toEnum(strEngine);
        if (engine == null) {
            throw new InvalidInputException("Valid value for engine is expected as input");
        }

        final Specification spec = new Specification(engine, Color.toEnum(strColor));
        final Car car = carManufacturer.manufactureCar(spec);
        if (car != null) {
            // return Response.ok().entity(car).build();
            final URI uri = uriInfo.getBaseUriBuilder().path(CarResource.class)
                    .path(CarResource.class, "getCar")
                    .build(car.getIdentifier());
            final JsonObject carObj = CarToJsonObject(car);
            return Response.created(uri).entity(carObj).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/{id}")
    public Response getCar(@PathParam("id") final String id) {
        final Car car = carManufacturer.retrieveCar(id);
        if (car != null) {
            final JsonObject carObj = CarToJsonObject(car);
            return Response.ok().entity(carObj).build();
        }
        return Response.noContent().build();
    }

    public static final JsonObject CarToJsonObject(final Car car) {
        return Json.createObjectBuilder()
                .add("id", car.getIdentifier())
                .add("color", car.getColor().toString())
                .add("engine", car.getEngineType().toString())
                .build();
    }
}
