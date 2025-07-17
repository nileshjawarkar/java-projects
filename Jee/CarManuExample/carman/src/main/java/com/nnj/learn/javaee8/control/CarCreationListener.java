package com.nnj.learn.javaee8.control;

import java.util.logging.Logger;

import jakarta.enterprise.event.Observes;

import com.nnj.learn.javaee8.entity.CarCreated;

public class CarCreationListener {
	static Logger LOGGER = Logger.getLogger(CarCreationListener.class.getName());
	public void onCarCreation(@Observes final CarCreated carCreatedEvent) {
		LOGGER.info("Car created with id - \'" + carCreatedEvent.getIdentifier() + "\'");
	}
}
