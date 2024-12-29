package com.nnj.learn.javaee8.control;

import java.util.logging.Logger;

import com.nnj.learn.javaee8.entity.CarCreated;

import jakarta.enterprise.event.Observes;

public class CarCreationListener {
	static Logger LOGGER = Logger.getLogger(CarCreationListener.class.getName());
	public void onCarCreation(@Observes CarCreated carCreatedEvent) {
		LOGGER.info("Car created with id - \'" + carCreatedEvent.getIdentifier() + "\'");
	}
}
