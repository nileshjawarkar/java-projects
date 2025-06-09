package com.nnj.learn.jee.webapi;

import java.net.URI;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import com.nnj.learn.jee.boundary.CarManufacturer;
import com.nnj.learn.jee.entity.Car;
import com.nnj.learn.jee.entity.Specification;

@Path("/car")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarRWService {
    @Inject
    CarManufacturer carManufacturer;

    @Context
    UriInfo uriInfo;

    @POST
    public Response createCar(final Specification spec) {
        final Car car = carManufacturer.manufactureCar(spec);
        final URI uri = uriInfo.getBaseUriBuilder().path(CarRWService.class).path(CarRWService.class, "getCar")
            .build(car.getId());
        return Response.created(uri).build();
    }

    @GET
    @Path("{id}")
    public Response getCar(@PathParam("id") @DefaultValue("XYZ") final String id) {
        final Car car = carManufacturer.findCar(id);
        if(car == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        final String jsonCar = Json.createObjectBuilder()
            .add("id", car.getId().toString())
            .add("color", car.getColor().toString())
            .add("engineType", car.getEngineType().toString())
            .build().toString();
        return Response.ok(jsonCar).build();
    }

    @GET
    public Response getAllCars() {
        final List<Car> cars = carManufacturer.findAll();
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (final Car car : cars) {
            /*
            final JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", car.getId().toString())
                .add("color", car.getColor().toString())
                .add("engineType", car.getEngineType().toString())
                .build();
            arrayBuilder.add(jsonObject); */
            arrayBuilder.add(Json.createValue(car.getId().toString()));
        }

        if(cars.size() > 0) {
            return Response.ok(arrayBuilder.build().toString()).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
