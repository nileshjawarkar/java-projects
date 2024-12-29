package com.nnj.learn.jee.control.events;

public class CarCreated {

    private final String id;

    public CarCreated(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
