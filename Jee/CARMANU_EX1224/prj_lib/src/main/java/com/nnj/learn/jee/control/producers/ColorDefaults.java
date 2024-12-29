package com.nnj.learn.jee.control.producers;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import com.nnj.learn.jee.control.producers.qualifiers.Electric;
import com.nnj.learn.jee.entity.Color;

public class ColorDefaults {

    @Produces
    @Named("default")
    public Color defaultColor() {
        return Color.RED;
    }

    @Produces
    @Electric
    // -- @Named("electric")
    public Color defaultElectricColor() {
        return Color.GREY;
    }
}
