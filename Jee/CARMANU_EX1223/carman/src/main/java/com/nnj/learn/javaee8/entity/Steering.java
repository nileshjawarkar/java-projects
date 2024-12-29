package com.nnj.learn.javaee8.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "steerings")
public class Steering {

	@Id
	private String id;

	@Enumerated(EnumType.STRING)
	private SteeringType steeringType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SteeringType getSteeringType() {
		return steeringType;
	}

	public void setSteeringType(SteeringType steeringType) {
		this.steeringType = steeringType;
	}
}
