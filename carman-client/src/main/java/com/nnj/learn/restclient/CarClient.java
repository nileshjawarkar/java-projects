package com.nnj.learn.restclient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.nnj.learn.restclient.entity.Car;
import com.nnj.learn.restclient.entity.Specification;

public interface CarClient {
	List<String> GetCars() throws URISyntaxException, IOException, InterruptedException;
	Car GetCar(String id) throws URISyntaxException, IOException, InterruptedException;
	Car CreateCar(Specification spec) throws URISyntaxException, IOException, InterruptedException;
}
