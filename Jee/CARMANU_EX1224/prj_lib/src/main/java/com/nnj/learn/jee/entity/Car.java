package com.nnj.learn.jee.entity;

public class Car {
    private String identifier;
    private EngineType engineType;
    private Color color;

    public Car(final String identifier, final EngineType engineType, final Color color) {
        this.identifier = identifier;
        this.engineType = engineType;
        this.color = color;
    }

    public Car(final String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(final EngineType engineType) {
        this.engineType = engineType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

}
