package com.nnj.learn.javaee8.boundry;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import com.nnj.learn.javaee8.control.CarFactory;
import com.nnj.learn.javaee8.control.CarRepository;
import com.nnj.learn.javaee8.entity.Car;
import com.nnj.learn.javaee8.entity.CarCreated;
import com.nnj.learn.javaee8.entity.EngineType;
import com.nnj.learn.javaee8.entity.InvalidEngine;
import com.nnj.learn.javaee8.entity.Specification;

@Stateless
public class CarManufacturer {
	
	@Inject
	CarFactory        carFactory;
	
	@Inject
	CarRepository carRepository;
	
	@Inject
	Event<CarCreated> carCreatedEvent;

	/*
	public CarManufacturer(CarFactory carFactory, 
		CarRepository carRepository) {
		this.carFactory = carFactory;
		this.carRepository = carRepository;
	} */

	public Car createCar(final Specification spec) {
		if (spec.getEngineType() == null || spec.getEngineType() == EngineType.UNKNOWN)
			throw new InvalidEngine("Engine type not supported.");
		final Car car = carFactory.createCar(spec);
		carRepository.save(car);
		carCreatedEvent.fire(new CarCreated(car.getId()));
		return car;
	}

	public Car retrieveCar(final String id) {
		return carRepository.findById(id);
	}

	public List<Car> retrieveCars() {
		return carRepository.getAll(null, null);
	}

	public List<Car> retrieveCars(final String filterByAttr, final String filterByValue) {
		return carRepository.getAll(filterByAttr, filterByValue);
	}
}
