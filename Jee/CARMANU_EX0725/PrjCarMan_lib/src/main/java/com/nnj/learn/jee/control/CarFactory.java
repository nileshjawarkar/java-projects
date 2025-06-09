package com.nnj.learn.jee.control;

import jakarta.ejb.Stateless;

import com.nnj.learn.jee.entity.Car;
import com.nnj.learn.jee.entity.Specification;

@Stateless
public class CarFactory {
    public Car createCar(final Specification spec) {
        final Car car = new Car();
        //-- car.setId(UUID.randomUUID().toString());
        car.setEngineType(spec.getEngineType());
        car.setColor(spec.getColor());
        return car;
    }
}
