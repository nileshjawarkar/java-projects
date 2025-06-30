package com.nnj.learn.javaee8.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Seats")
public class Seat {
	@Id
	private String id;
	
	@Enumerated(EnumType.STRING)
	private SeatMaterial seatMaterial;
	
	@Embedded
	private SeatBelt seatBelt;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public SeatMaterial getSeatMaterial() {
		return seatMaterial;
	}

	public void setSeatMaterial(final SeatMaterial seatMaterial) {
		this.seatMaterial = seatMaterial;
	}

	public SeatBelt getSeatBelt() {
		return seatBelt;
	}

	public void setSeatBelt(final SeatBelt seatBelt) {
		this.seatBelt = seatBelt;
	}
}
