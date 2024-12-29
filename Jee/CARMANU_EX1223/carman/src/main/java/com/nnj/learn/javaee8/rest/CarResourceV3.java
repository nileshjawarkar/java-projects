package com.nnj.learn.javaee8.rest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

import com.nnj.learn.javaee8.boundry.CarManufacturer;
import com.nnj.learn.javaee8.entity.Car;
import com.nnj.learn.javaee8.entity.Color;
import com.nnj.learn.javaee8.entity.EngineType;
import com.nnj.learn.javaee8.entity.Specification;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
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
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("v3/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarResourceV3 {
    static final Logger LOGGER = Logger.getLogger(CarResourceV3.class.getName());

    @Inject
    CarManufacturer carManufacturer;
    @Context
    UriInfo uriInfo;
    @Inject
    Color defaultColor;

    @Resource
    ManagedExecutorService mes;

    @GET
    public CompletionStage<Response> getCars(
            @QueryParam("attr") @DefaultValue("") final String filterByAttr,
            @QueryParam("value") @DefaultValue("") final String filterByValue) {
        return CompletableFuture.supplyAsync(() -> {
            final List<Car> cars = carManufacturer.retrieveCars(filterByAttr, filterByValue);
            if (cars == null || cars.size() == 0) {
                return Response.noContent().build();
            }

            LOGGER.info("Number of cars - " + cars.size());
            JsonArray jsonArray = cars.stream()
                    .map(car -> Json.createValue(car.getId()))
                    .collect(JsonCollectors.toJsonArray());
            return Response.ok().entity(jsonArray).build();
        }, mes);
    }

    @GET
    @Path("{id}")
    public Response getCar(@PathParam("id") @DefaultValue("zyx") String id) {
        Car car = carManufacturer.retrieveCar(id);
        if (car == null) {
            return Response.noContent().build();
        }

        JsonObject jsonObj = Json.createObjectBuilder()
                .add("color", car.getColor().name())
                .add("engineType", car.getEngineType().name())
                .add("id", car.getId())
                .build();
        return Response.ok().entity(jsonObj).build();
    }

    @POST
    public void createCar(@NotNull final JsonObject jsonSpec, @Suspended AsyncResponse response) {
        final String tmpCarUrl = uriInfo.getBaseUriBuilder()
                .path(CarResourceV3.class)
                .path(CarResourceV3.class, "getCar")
                .build("CARID")
                .toString();
        mes.execute(() -> {
            response.resume(createCar(jsonSpec, tmpCarUrl));
        });
    }

    Response createCar(JsonObject jsonSpec, final String tmpCarUrl) {
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

            String newCarURL = tmpCarUrl.replace("CARID", car.getId());
            return Response.status(Response.Status.CREATED)
                    .header("location", newCarURL)
                    .entity(car)
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
