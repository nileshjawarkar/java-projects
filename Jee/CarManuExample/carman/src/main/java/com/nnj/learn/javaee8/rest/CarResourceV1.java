package com.nnj.learn.javaee8.rest;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.nnj.learn.javaee8.boundry.CarManufacturer;
import com.nnj.learn.javaee8.entity.Car;
import com.nnj.learn.javaee8.entity.InvalidEngine;
import com.nnj.learn.javaee8.entity.Specification;

@Path("v1/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarResourceV1 {
	
	@Inject
	CarManufacturer carManufacturer;
	
	@GET
	public List<Car> getCars() {
		return carManufacturer.retrieveCars();
	}
	
	@GET
	@Path("{id}")
	public Car getCar(@PathParam("id") @DefaultValue("zyx") final String id) {
		return carManufacturer.retrieveCar(id);
	}
	
	@POST
	public Car createCar(final Specification spec) throws InvalidEngine {
		return carManufacturer.createCar(spec);
	}
}
