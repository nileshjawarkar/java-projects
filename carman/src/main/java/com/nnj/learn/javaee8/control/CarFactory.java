package com.nnj.learn.javaee8.control;

import java.util.UUID;

import com.nnj.learn.javaee8.entity.Car;
import com.nnj.learn.javaee8.entity.Specification;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class CarFactory {

	@Inject
	@AppConfig("car.name_prefix")
	String prefix;
	
	public Car createCar(Specification spec) {
		Car c =  new Car();
		c.setColor(spec.getColor());
		c.setEngine(spec.getEngineType());
		c.setIdentifier(prefix + UUID.randomUUID().toString());
		return c;
	}
}
