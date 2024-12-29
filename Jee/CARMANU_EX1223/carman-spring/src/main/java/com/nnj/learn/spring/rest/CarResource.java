package com.nnj.learn.spring.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nnj.learn.spring.entity.Specification;

import com.nnj.learn.spring.boundry.CarManufacturer;
import com.nnj.learn.spring.entity.Car;
import com.nnj.learn.spring.entity.InvalidEngine;

@RestController
@RequestMapping("/v1/cars")
public class CarResource {
	
	private CarManufacturer carMan;
	
	@Autowired
	public CarResource(CarManufacturer theCarMan) {
		carMan = theCarMan;
	}
	
	@GetMapping("")
	List<Car> getCars() {
		return carMan.retrieveCars();
	}
	
	@GetMapping("/{id}")
	public Car getCar(@PathVariable String id) {
		return carMan.retrieveCar(id);
	}
	
	@PostMapping("")
	public Car createCar(@RequestBody Specification spec) throws InvalidEngine {
		return carMan.createCar(spec);
	}
}
