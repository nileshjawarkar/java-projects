package com.nnj.learn.jee.entity;

import jakarta.enterprise.event.Event;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.nnj.learn.jee.control.CarFactory;
import com.nnj.learn.jee.control.events.CarCreated;

public class CarTest {

    @Mock
    Color defaultColor;

    @Mock
    Event<CarCreated> carCreatedEvent;

    @InjectMocks
    CarFactory carFactory = new CarFactory();

    @Test
    public void test_name() {
        final Car car = carFactory.createCar(new Specification(EngineType.DIESEL, Color.RED));
        assertNotNull(car); 
    }

}
