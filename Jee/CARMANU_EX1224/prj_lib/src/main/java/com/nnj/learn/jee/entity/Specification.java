package com.nnj.learn.jee.entity;

public class Specification {
    private EngineType engineType;
    private Color color;

    public Specification() {
    }

    public Specification(final EngineType engineType, final Color color) {
        this.engineType = engineType;
        this.color = color;
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
