package com.nnj.learn.jee.control;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.nnj.learn.jee.entity.Car;

@Stateless
public class CarRepository {

    @PersistenceContext
    EntityManager entityManager;

    public void store(final Car car) {
        entityManager.persist(car);
    }

    public Car findCar(final UUID id) {
        //-- return entityManager.find(Car.class, id);
        return entityManager.createNamedQuery(Car.FIND_A_CAR, Car.class)
            .setParameter("Id", id).getResultList().getFirst();
    }

    public List<Car> findAll() {
        return entityManager.createNamedQuery(Car.FIND_ALL_CARS, Car.class).getResultList();
    }
}
