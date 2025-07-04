package com.nnj.learn.jee.entity;

import jakarta.validation.constraints.NotNull;

/**
 * Specification
 */
public class Specification {
    @NotNull
    private Color color;
    @NotNull
    private EngineType engineType;
    private Category category;

    /*
     * Required for JAX-RS
     */
    public Specification() {}

    public Specification(final EngineType engineType, final Color color) {
        this.engineType = engineType;
        this.color = color;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

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
