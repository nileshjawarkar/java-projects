package com.nnj.learn.javaee8.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
@NamedQuery(name = Car.FIND_ALL_CARS, query = "select c from Car c")
@NamedQuery(name = Car.FIND_A_CAR, query = "select c from Car c where c.id = :carId")
public class Car {
	
	public static final String FIND_ALL_CARS = "Car.FindAll";
	public static final String FIND_A_CAR = "Car.FindACar";
	
	@Id
	private String id;
	
	@Enumerated(EnumType.STRING)
	private EngineType engineType;
	
	@Enumerated(EnumType.STRING)
	private Color color;

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
