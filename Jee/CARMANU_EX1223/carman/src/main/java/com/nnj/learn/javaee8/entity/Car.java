package com.nnj.learn.javaee8.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name = "cars")
@NamedQuery(name = Car.FIND_ALL_CARS, query = "select c from Car c")
@NamedQuery(name = Car.FIND_A_CAR, query = "select c from Car c where c.id = :carId")
public class Car {
    public static final String FIND_ALL_CARS = "Car.FindAll";
    public static final String FIND_A_CAR = "Car.FindACar";

    @Id
    @TableGenerator(name = "CARIDGEN", pkColumnName = "ID", pkColumnValue = "CARID",
        valueColumnName = "ID_VALUE", initialValue = 100, allocationSize = 10)
    @GeneratedValue(generator = "CARIDGEN", strategy = GenerationType.TABLE)
    private String id;

    @Enumerated(EnumType.STRING) private EngineType engineType;

    @Enumerated(EnumType.STRING) private Color color;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "car", nullable = false)
    private Set<Seat> seats;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "car", nullable = false)
    private Steering steering;

    public Steering getSteering() {
        return steering;
    }

    public void setSteering(final Steering steering) {
        this.steering = steering;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(final Set<Seat> seats) {
        this.seats = seats;
    }

    public Color getColor() {
        return color;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public String getId() {
        return id;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public void setEngine(final EngineType engineType) {
        this.engineType = engineType;
    }

    public void setEngineType(final EngineType engineType) {
        this.engineType = engineType;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setIdentifier(final String id) {
        this.id = id;
    }
}
