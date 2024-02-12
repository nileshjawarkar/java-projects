package com.nnj.learn.restclient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nnj.learn.restclient.entity.Car;
import com.nnj.learn.restclient.entity.Color;
import com.nnj.learn.restclient.entity.EngineType;
import com.nnj.learn.restclient.entity.Specification;

public class CarClientApache implements CarClient {

	private final String strURL;

	public CarClientApache(final String baseUrl) {
		strURL = baseUrl + "/api/v3/cars";
	}

	@Override
	public List<String> GetCars() throws URISyntaxException, IOException, InterruptedException {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet httpGet = new HttpGet(strURL);
			httpGet.addHeader("Accept", "application/json");
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				if (response != null && response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					String jsonStr = EntityUtils.toString(entity);
					if (jsonStr != null && jsonStr.length() > 0) {
						if (jsonStr != null) {
							List<String> cars = new ArrayList<String>();
							JSONArray jarray = new JSONArray(jsonStr);
							for (int i = 0; i < jarray.length(); i += 1) {
								cars.add(jarray.getString(i));
							}
							return cars;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public Car GetCar(String id) throws URISyntaxException, IOException, InterruptedException {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet httpGet = new HttpGet(strURL + "/" + id);
			httpGet.addHeader("Accept", "application/json");
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				if (response != null && response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					String jsonStr = EntityUtils.toString(entity);
					if(jsonStr != null) {
						JSONObject jobj =  new JSONObject(jsonStr);
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
			}
		}
		return null;
	}

	@Override
	public Car CreateCar(Specification spec) throws URISyntaxException, IOException, InterruptedException {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(strURL);
			httpPost.addHeader("Accept", "application/json");
			
			String jsonObject = new JSONObject()
					.put("color", spec.getColor().name())
					.put("engineType", spec.getEngineType().name())
					.toString();
			
			StringEntity entity = new StringEntity(jsonObject,
					ContentType.create("application/json", Consts.UTF_8));
					entity.setChunked(true);
					
			httpPost.setEntity(entity);
			
			try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
				if (response != null && response.getStatusLine().getStatusCode() == 201) {
					HttpEntity rentity = response.getEntity();
					String jsonStr = EntityUtils.toString(rentity);
					if(jsonStr != null) {
						JSONObject jobj =  new JSONObject(jsonStr);
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
			}
					
		}
		return null;
	}

}
