package com.nnj.learn.restclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nnj.learn.restclient.entity.Car;
import com.nnj.learn.restclient.entity.Color;
import com.nnj.learn.restclient.entity.EngineType;
import com.nnj.learn.restclient.entity.Specification;

public class CarClientDefault implements CarClient {

	private final String strURL;

	public CarClientDefault(final String baseUrl) {
		strURL = baseUrl + "/api/v3/cars";
	}

	@Override
	public List<String> GetCars() throws URISyntaxException, IOException, InterruptedException {
		HttpRequest req = HttpRequest.newBuilder().uri(new URI(strURL))
				.header("content-type", "application/json")
				.GET().build();
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
		
		List<String> cars = null;
		if (resp != null && resp.statusCode() == 200) {
			String jsonResp = resp.body();
			if(jsonResp != null) {
				cars = new ArrayList<String>();
				JSONArray jarray = new JSONArray(jsonResp);
				for(int i = 0; i < jarray.length(); i += 1) {
					cars.add(jarray.getString(i));
				}
			}
		}
		return cars;
	}

	@Override
	public Car GetCar(String id) throws URISyntaxException, IOException, InterruptedException {
		HttpRequest req = HttpRequest.newBuilder().uri(new URI(strURL + "/" + id))
				.header("content-type", "application/json")
				.header("accept", "application/json")
				.GET().build();
		
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
		if (resp != null && resp.statusCode() == 200) {
			String jsonResp = resp.body();
			if(jsonResp != null) {
				JSONObject jobj =  new JSONObject(jsonResp);
				String carId = jobj.getString("id");
				String carColor = jobj.getString("color");
				String carEngine = jobj.getString("engineType");
				
				Car c = new Car();
				c.setId(carId);
				c.setColor(Color.valueOf(carColor));
				c.setEngine(EngineType.valueOf(carEngine));
				return c;				
			}
		}
		return null;
	}

	@Override
	public Car CreateCar(Specification spec) throws URISyntaxException, IOException, InterruptedException {
		String jsonObject = new JSONObject()
				.put("color", spec.getColor().name())
				.put("engineType", spec.getEngineType().name())
				.toString();
				
		HttpRequest req = HttpRequest.newBuilder().uri(new URI(strURL))
				.header("content-type", "application/json")
				.header("accept", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonObject)).build();
		
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
		if (resp != null && resp.statusCode() == 201) {
			String jsonResp = resp.body();
			if(jsonResp != null) {
				JSONObject jobj =  new JSONObject(jsonResp);
				String carId = jobj.getString("id");
				String carColor = jobj.getString("color");
				String carEngine = jobj.getString("engineType");
				
				Car c = new Car();
				c.setId(carId);
				c.setColor(Color.valueOf(carColor));
				c.setEngine(EngineType.valueOf(carEngine));
				return c;				
			}
		}		
		return null;
	}

}
