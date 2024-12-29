package com.nnj.learn.spring.boundry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.nnj.learn.spring.control.CarFactory;
import com.nnj.learn.spring.control.CarRepository;
import com.nnj.learn.spring.entity.Car;
//-- import com.nnj.learn.spring.entity.CarCreated;
import com.nnj.learn.spring.entity.EngineType;
import com.nnj.learn.spring.entity.InvalidEngine;
import com.nnj.learn.spring.entity.Specification;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CarManufacturer {
	private CarFactory        carFactory;
	private CarRepository carRepository;
	
	@Autowired
	public CarManufacturer(CarFactory theCarFactory, CarRepository theCarRepository) {
		carFactory = theCarFactory;
		carRepository = theCarRepository;
	}	
	
	//-- Event<CarCreated> carCreatedEvent;

	/*
	public CarManufacturer(CarFactory carFactory, 
		CarRepository carRepository) {
		this.carFactory = carFactory;
		this.carRepository = carRepository;
	} */

	public Car createCar(final Specification spec) throws InvalidEngine {
		if (spec.getEngineType() == EngineType.UNKNOWN)
			throw new InvalidEngine();
		final Car car = carFactory.createCar(spec);
		carRepository.save(car);
		//-- carCreatedEvent.fire(new CarCreated(car.getId()));
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
