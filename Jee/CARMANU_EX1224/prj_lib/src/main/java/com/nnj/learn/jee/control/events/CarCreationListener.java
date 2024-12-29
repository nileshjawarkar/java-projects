package com.nnj.learn.jee.control.events;

import jakarta.enterprise.event.Observes;

public class CarCreationListener {
    public void onCreation(@Observes final CarCreated carCreatedEvent) {
        System.out.println("Created car with id = " + carCreatedEvent.getId());
    }
}
