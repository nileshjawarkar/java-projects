package com.nnj.learn.jee.control;

import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.nnj.learn.jee.control.events.CarCreated;
import com.nnj.learn.jee.control.producers.qualifiers.Electric;
import com.nnj.learn.jee.entity.Car;
import com.nnj.learn.jee.entity.Color;
import com.nnj.learn.jee.entity.EngineType;
import com.nnj.learn.jee.entity.Specification;

@Stateless
public class CarFactory {

    @Inject
    @Named("default")
    Color defaultColor;

    @Inject
    @Electric
    //-- @Named("electric")
    Color defaultElectricColor;

    @Inject
    Event<CarCreated> carCreatedEvent;

    public Car createCar(final Specification spec) {
        final Car car = new Car(UUID.randomUUID().toString());
        final EngineType type = spec.getEngineType();
        Color color = spec.getColor();
        if (color == null) {
            color = defaultColor;
            if (type == EngineType.ELECTRIC) {
                color = defaultElectricColor;
            }
        }
        car.setColor(color);
        car.setEngineType(type);
//        carCreatedEvent.fire(new CarCreated(car.getIdentifier()));
        return car;
    }
}
