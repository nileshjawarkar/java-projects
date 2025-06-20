package com.nnj.learn.jee.boundary;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

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
        if(spec.getEngineType() == null || spec.getColor() == null) {
            throw new BadRequestException("Invalid input engineType or color");
        }
        final Car car = carFactory.createCar(spec);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(" Created => " + car);
        }
        carRepository.store(car);
        return car;
    }

    public Car findCar(final String id) {
        final UUID uuid = UUID.fromString(id);
        if(uuid == null) {
            throw new BadRequestException("Invalid input id [" + id + "]");
        }

        final Car car = carRepository.findCar(uuid);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(" Retrieved => " + car);
        }
        return car;
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }
}
