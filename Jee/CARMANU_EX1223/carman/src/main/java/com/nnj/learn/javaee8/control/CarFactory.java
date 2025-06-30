package com.nnj.learn.javaee8.control;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import com.nnj.learn.javaee8.entity.Car;
import com.nnj.learn.javaee8.entity.Seat;
import com.nnj.learn.javaee8.entity.SeatBelt;
import com.nnj.learn.javaee8.entity.SeatBeltModel;
import com.nnj.learn.javaee8.entity.SeatMaterial;
import com.nnj.learn.javaee8.entity.Specification;
import com.nnj.learn.javaee8.entity.Steering;
import com.nnj.learn.javaee8.entity.SteeringType;

@Dependent
public class CarFactory {

	@Inject
	@AppConfig("car.name_prefix")
	String prefix;

	public Car createCar(final Specification spec) {
		final Car c = new Car();
		c.setColor(spec.getColor());
		c.setEngine(spec.getEngineType());
		//-- c.setIdentifier(prefix + UUID.randomUUID().toString());

		final Seat seat1 = new Seat();
		seat1.setId(UUID.randomUUID().toString());

		final SeatBelt sb = new SeatBelt();
		sb.setSeatModel(SeatBeltModel.V1);
		seat1.setSeatBelt(sb);
		seat1.setSeatMaterial(SeatMaterial.LEATHER);
		
		final Seat seat2 = new Seat();
		seat2.setId(UUID.randomUUID().toString());

		final SeatBelt sb2 = new SeatBelt();
		sb2.setSeatModel(SeatBeltModel.V2);
		seat2.setSeatBelt(sb2);
		seat2.setSeatMaterial(SeatMaterial.LEATHER);
		
		final Set<Seat> seats = new HashSet<Seat>();
		seats.add(seat1);
		seats.add(seat2);
		c.setSeats(seats);
		
		final Steering steering = new Steering();
		steering.setId(UUID.randomUUID().toString());
		steering.setSteeringType(SteeringType.POWER_STEERING);
		c.setSteering(steering);
		return c;
	}
}
