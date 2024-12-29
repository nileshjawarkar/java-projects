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
    private CarFactory factory;
    @Inject
    private CarRepository repo;

    public Car manufactureCar(final Specification spec) {
        final Car car = factory.createCar(spec);
        repo.store(car);
        return car;
    }

    public List<Car> retrieveAllCars(final Integer limit, final Integer offset) {
        return repo.getCars(limit, offset);
    }

    public Car retrieveCar(final String id) {
        return repo.getCar(id);
    }

}
