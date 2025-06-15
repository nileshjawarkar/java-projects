package com.nnj.learn.jee.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class SeatBelt {

    @Enumerated(EnumType.STRING)
    private SeatBeltModel model;

    public SeatBeltModel getModel() {
        return model;
    }

    public void setModel(final SeatBeltModel model) {
        this.model = model;
    }
}

