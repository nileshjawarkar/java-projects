package com.nnj.learn.javaee8.rest;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import com.nnj.learn.javaee8.boundry.CarManufacturer;
import com.nnj.learn.javaee8.control.SpeatialEditionColor;
import com.nnj.learn.javaee8.entity.Car;
import com.nnj.learn.javaee8.entity.Color;
import com.nnj.learn.javaee8.entity.EngineType;
import com.nnj.learn.javaee8.entity.Specification;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
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

@Path("v2/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarResourceV2 {

    static final Logger LOGGER = Logger.getLogger(CarResourceV2.class.getName());

    @Inject
    CarManufacturer carManufacturer;

    @Context
    UriInfo uriInfo;

    @Inject
    // @Named("SpeatialEdition")
    @SpeatialEditionColor
    Color defaultColor;

    @GET
    public Response getCars(@QueryParam("attr") @DefaultValue("") final String filterByAttr,
            @QueryParam("value") @DefaultValue("") final String filterByValue) {
        final List<Car> cars = carManufacturer.retrieveCars(filterByAttr, filterByValue);
        if (cars == null || cars.size() == 0) {
            return Response.noContent().build();
        }

        LOGGER.info("Number of cars - " + cars.size());
        JsonArray jsonArray = cars.stream().map(car -> Json.createValue(car.getId()))
                .collect(JsonCollectors.toJsonArray());
        return Response.ok().entity(jsonArray).build();
    }

    @GET
    @Path("{id}")
    public Response getCar(@PathParam("id") @DefaultValue("zyx") String id) {
        Car car = carManufacturer.retrieveCar(id);
        if (car == null) {
            return Response.noContent().build();
        }

        JsonObject jsonObj = Json.createObjectBuilder().add("color", car.getColor().name())
                .add("engineType", car.getEngineType().name()).add("id", car.getId()).build();
        return Response.ok().entity(jsonObj).build();
    }

    @POST
    public Response createCar(@NotNull final JsonObject jsonSpec) {
        final String strColor = (jsonSpec.containsKey("color") ? jsonSpec.getString("color") : null);
        final String strEngineType = (jsonSpec.containsKey("engineType") ? jsonSpec.getString("engineType") : null);

        if (strEngineType != null) {
            final Specification spec = new Specification();
            EngineType engineType = EngineType.UNKNOWN;
            try {
                engineType = EngineType.valueOf(strEngineType);
            } catch (Exception e) {
                LOGGER.severe("Engine type - \'" + strEngineType + "\' not valid.");
            }
            spec.setEngineType(engineType);

            spec.setColor(strColor == null ? defaultColor : Color.valueOf(strColor));
            final Car car = carManufacturer.createCar(spec);
            final URI uri = uriInfo.getBaseUriBuilder().path(CarResourceV2.class).path(CarResourceV2.class, "getCar")
                    .build(car.getId());
            return Response.created(uri).entity(car).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
