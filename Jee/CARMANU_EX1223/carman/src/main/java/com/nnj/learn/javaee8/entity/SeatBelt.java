package com.nnj.learn.javaee8.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class SeatBelt {
	
	@Enumerated(EnumType.STRING)
	private SeatBeltModel seatModel;

	public SeatBeltModel getSeatModel() {
		return seatModel;
	}

	public void setSeatModel(final SeatBeltModel seatModel) {
		this.seatModel = seatModel;
	}
}
