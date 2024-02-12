package com.nnj.learn.restclient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.nnj.learn.restclient.entity.Car;
import com.nnj.learn.restclient.entity.Color;
import com.nnj.learn.restclient.entity.EngineType;
import com.nnj.learn.restclient.entity.Specification;

public class Main {

	public static void main(String[] args) {
		CarClient carClient = new CarClientApache("http://localhost:8080/carman");
		try {
			List<String> cars = carClient.GetCars();
			for(String c : cars) {
				System.out.println("id = " + c);
				
				Car car = carClient.GetCar(c);
				System.out.println(car);
			}
			
			Specification spec = new Specification();
			spec.setColor(Color.BLUE);
			spec.setEngineType(EngineType.ELECTRIC);
			
			Car carNew = carClient.CreateCar(spec);
			System.out.println(carNew);
			

		} catch (URISyntaxException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
