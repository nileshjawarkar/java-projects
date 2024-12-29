package com.nnj.learn.javaee8.control;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.nnj.learn.javaee8.entity.Car;
import com.nnj.learn.javaee8.entity.Color;
import com.nnj.learn.javaee8.entity.EngineType;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;

@Singleton
public class CarRepositoryInMemImpl implements CarRepository {
	private List<Car> list = new ArrayList<>();
	
	static final Logger LOGGER = Logger.getLogger(CarRepositoryInMemImpl.class.getName());

	//public CarRepositoryInMemImpl() {
	@PostConstruct
	public void Init() {
		final Car c1 = new Car();
		c1.setId("3794bab4-a59f-47a6-a4fb-da597d46f782");
		c1.setColor(Color.BLUE);
		c1.setEngine(EngineType.DIESEL);
		list.add(c1);

		final Car c2 = new Car();
		c2.setId(UUID.randomUUID().toString());
		c2.setColor(Color.WHITE);
		c2.setEngine(EngineType.PETROL);
		list.add(c2);
		
		LOGGER.info("Init called using @PostConstruct.");
	}
	
	@PreDestroy
	public void clean() {
		list.clear();
	}

	@Override
	public Car findById(final String id) {
		for (final Car c : list) {
			final String cid = c.getId();
			if (cid != null & cid.equals(id))
				return c;
		}
		return null;
	}

	@Override
	public List<Car> getAll(final String filterByAttr, final String filterByValue) {
		if (filterByAttr == null || filterByAttr.equals("") || filterByValue == null || filterByValue.equals(""))
			return list;

		return list.stream().filter(c -> {
			if (filterByAttr.equals("color"))
				return c.getColor() == Color.valueOf(filterByValue);
			if (filterByAttr.equals("engineType"))
				return c.getEngineType() == EngineType.valueOf(filterByValue);
			return false;
		}).collect(Collectors.toList());
	}

	@Override
	public void save(final Car car) {
		list.add(car);
	}

}
