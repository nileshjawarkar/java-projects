package com.nnj.learn.javaee8.boundry;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

import com.nnj.learn.javaee8.control.CarFactory;
//- import com.nnj.learn.javaee8.control.CarRepository;
import com.nnj.learn.javaee8.entity.Car;
import com.nnj.learn.javaee8.entity.CarCreated;
import com.nnj.learn.javaee8.entity.Color;
import com.nnj.learn.javaee8.entity.EngineType;
import com.nnj.learn.javaee8.entity.InvalidEngine;
import com.nnj.learn.javaee8.entity.Specification;

@Stateless
public class CarManufacturer {

	@Inject
	CarFactory carFactory;

	/*
	 * @Inject CarRepository carRepository;
	 */

	@PersistenceContext
	EntityManager entityManager;

	@Inject
	Event<CarCreated> carCreatedEvent;

	/*
	 * public CarManufacturer(CarFactory carFactory, CarRepository carRepository) {
	 * this.carFactory = carFactory; this.carRepository = carRepository; }
	 */

	public Car createCar(final Specification spec) {
		if (spec.getEngineType() == null || spec.getEngineType() == EngineType.UNKNOWN)
			throw new InvalidEngine("Engine type not supported.");
		final Car car = carFactory.createCar(spec);
		// -- carRepository.save(car);
		entityManager.persist(car);
		carCreatedEvent.fire(new CarCreated(car.getId()));
		return car;
	}

	public Car retrieveCar(final String id) {
		// -- return carRepository.findById(id);
		try {
			return entityManager.createNamedQuery(Car.FIND_A_CAR, Car.class).setParameter("carId", id)
					.getSingleResult();
		} catch (NoResultException e) {
		}
		return null;
	}

	public List<Car> retrieveCars() {
		// -- return carRepository.getAll(null, null);
		return entityManager.createNamedQuery(Car.FIND_ALL_CARS, Car.class).getResultList();
	}

	public List<Car> retrieveCars(final String filterByAttr, final String filterByValue) {
		if (filterByAttr == null || filterByAttr.equals("") || filterByValue == null || filterByValue.equals("")) {
			return retrieveCars();
		}

		boolean isColorAttr = filterByAttr.equals("color");
		boolean isETAttr = filterByAttr.equals("engineType");
		if (isColorAttr || isETAttr) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Car> cQuery = cb.createQuery(Car.class);
			Root<Car> c = cQuery.from(Car.class);

			/*
			 * Being more specific about type is more secure
			 * ---------------------------------------------- ParameterExpression<Enum>
			 * paramEnumType = cb.parameter(Enum.class);
			 * cQuery.select(c).where(cb.equal(c.get(filterByAttr), paramEnumType));
			 * TypedQuery<Car> query = entityManager.createQuery(cQuery);
			 */

			TypedQuery<Car> query = null;
			if (isETAttr) {
				ParameterExpression<EngineType> paramEngineType = cb.parameter(EngineType.class);
				cQuery.select(c).where(cb.equal(c.get(filterByAttr), paramEngineType));
				query = entityManager.createQuery(cQuery);
				EngineType et = EngineType.valueOf(filterByValue);
				query.setParameter(paramEngineType, et);
			} else {
				ParameterExpression<Color> paramColorType = cb.parameter(Color.class);
				cQuery.select(c).where(cb.equal(c.get(filterByAttr), paramColorType));
				query = entityManager.createQuery(cQuery);
				Color color = Color.valueOf(filterByValue);
				query.setParameter(paramColorType, color);
			}
			return query.getResultList();
		}
		return null;
		// return carRepository.getAll(filterByAttr, filterByValue);
	}
}
