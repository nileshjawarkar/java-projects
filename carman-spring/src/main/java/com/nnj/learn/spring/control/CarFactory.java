package com.nnj.learn.spring.control;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nnj.learn.spring.entity.Car;
import com.nnj.learn.spring.entity.Specification;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CarFactory {

	public Car createCar(Specification spec) {
		Car c =  new Car();
		c.setColor(spec.getColor());
		c.setEngine(spec.getEngineType());
		c.setIdentifier(UUID.randomUUID().toString());
		return c;
	}
}
