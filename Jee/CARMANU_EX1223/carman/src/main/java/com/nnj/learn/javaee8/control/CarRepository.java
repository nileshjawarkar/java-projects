package com.nnj.learn.javaee8.control;

import java.util.List;

import com.nnj.learn.javaee8.entity.Car;

public interface CarRepository {
	public Car findById(final String id) ;
	public List<Car> getAll(final String filterByAttr, final String filterByValue) ;
	public void save(final Car car);
}
