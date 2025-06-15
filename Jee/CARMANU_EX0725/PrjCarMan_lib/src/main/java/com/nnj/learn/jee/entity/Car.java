package com.nnj.learn.jee.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
@NamedQuery(name = Car.FIND_ALL_CARS, query = "select c from Car c")
@NamedQuery(name = Car.FIND_A_CAR, query = "select c from Car c where c.id = :Id")
public class Car {
    public static final String FIND_ALL_CARS = "Car.FindAll";
    public static final String FIND_A_CAR = "Car.FindACar";

    //-- @GeneratedValue(strategy = GenerationType.AUTO)
    //-- @GeneratedValue(strategy = GenerationType.SEQUENCE)
    //-- @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //-- private UUID id;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "engine")
    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "car", nullable = false)
    private List<Seat> seats;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(final EngineType engineType) {
        this.engineType = engineType;
    }

    @Override
    public String toString() {
        return "Car {id=" + id + ", color=" + color + ", engineType=" + engineType + "}";
    }

    public static String getFindAllCars() {
        return FIND_ALL_CARS;
    }

    public static String getFindACar() {
        return FIND_A_CAR;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(final List<Seat> seats) {
        this.seats = seats;
    }
}
