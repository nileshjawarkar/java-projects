package com.nnj.learn.jee.entity;

/**
 * Specification
 */
public class Specification {
    private Color color;
    private EngineType engineType;

    public Color getColor() {
        return color;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public void setEngineType(final EngineType engineType) {
        this.engineType = engineType;
    }
}
