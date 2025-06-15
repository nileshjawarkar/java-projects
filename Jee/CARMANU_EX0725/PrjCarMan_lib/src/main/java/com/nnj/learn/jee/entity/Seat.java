package com.nnj.learn.jee.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SeatMaterial material;

    @Embedded
    private SeatBelt belt;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

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

