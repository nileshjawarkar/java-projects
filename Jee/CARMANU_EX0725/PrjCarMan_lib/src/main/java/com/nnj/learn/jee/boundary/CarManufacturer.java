package com.nnj.learn.jee.boundary;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nnj.learn.jee.control.CarFactory;
import com.nnj.learn.jee.control.CarRepository;
import com.nnj.learn.jee.entity.Car;
import com.nnj.learn.jee.entity.Specification;

@Stateless
public class CarManufacturer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarManufacturer.class.getName());

    @Inject
    CarFactory carFactory;
    @Inject
    CarRepository carRepository;

    public Car manufactureCar(final Specification spec) {
        final Car car = carFactory.createCar(spec);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(" Created => " + car);
        }
        carRepository.store(car);
        return car;
    }

    public Car findCar(final String id) {
        final Car car = carRepository.findCar(Long.valueOf(id));
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(" Retrieved => " + car);
        }
        return car;
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }
}
