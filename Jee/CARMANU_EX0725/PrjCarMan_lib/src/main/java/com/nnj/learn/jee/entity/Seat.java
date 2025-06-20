package com.nnj.learn.jee.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private SeatMaterial material;

    @Embedded
    private SeatBelt belt;

    public SeatMaterial getMaterial() {
        return material;
    }

    public void setMaterial(final SeatMaterial material) {
        this.material = material;
    }

    public SeatBelt getBelt() {
        return belt;
    }

    public void setBelt(final SeatBelt belt) {
        this.belt = belt;
    }

}

