package com.nnj.learn.jee.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "steeting")
public class SteeringWheel extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private SteeringType type;

    public SteeringType getType() {
        return type;
    }

    public void setType(final SteeringType type) {
        this.type = type;
    }
}

