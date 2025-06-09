package com.nnj.learn.jee.boundary;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.nnj.learn.jee.control.CarFactory;
import com.nnj.learn.jee.control.CarRepository;
import com.nnj.learn.jee.entity.Car;
import com.nnj.learn.jee.entity.Specification;

@Stateless
public class CarManufacturer {

    @Inject
    CarFactory carFactory;
    @Inject
    CarRepository carRepository;

    public Car manufactureCar(final Specification spec) {
        final Car car = carFactory.createCar(spec);
        System.out.println(" Created => " + car);
        carRepository.store(car);
        return car;
    }

    public Car findCar(final String id) {
        final Car car = carRepository.findCar(Long.valueOf(id));
        System.out.println(" Retrieved => " + car);
        return car;
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }
}
