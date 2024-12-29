package com.nnj.learn.jee.control;

import java.util.List;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.nnj.learn.jee.control.repo.CarRepo;
import com.nnj.learn.jee.entity.Car;
import com.nnj.learn.jee.entity.Color;
import com.nnj.learn.jee.entity.EngineType;

/* Note : After marking this class as EJB, it can-not inherits from CarRepo. This is
 * because, it is already injecting same type inside it. which is
 * making jee-container confused.
 *
 * Observation - If default scope is used (when we do not mark it with any annotation),
 * suprisingly it works, but when we make it EJB it didnt.
 */

@Singleton
public class CarRepository /* implements CarRepo */ {

    @Inject
    @Named("in-db")
    CarRepo carRepo;

    @PostConstruct
    public void init() {
        carRepo.setup();

        final Car c1 = new Car(UUID.randomUUID().toString());
        c1.setColor(Color.RED);
        c1.setEngineType(EngineType.PETROL);
        store(c1);

        final Car c2 = new Car(UUID.randomUUID().toString());
        c2.setColor(Color.BLACK);
        c2.setEngineType(EngineType.DIESEL);
        store(c2);
    }

    @PreDestroy
    public void removeAll() {
        carRepo.cleanup();
    }

    public void store(final Car car) {
        carRepo.store(car);
    }

    public Car getCar(final String id) {
        return carRepo.getCar(id);
    }

    public List<Car> getCars(final Integer limit, final Integer offset) {
        return carRepo.getCars(limit, offset);
    }
}
