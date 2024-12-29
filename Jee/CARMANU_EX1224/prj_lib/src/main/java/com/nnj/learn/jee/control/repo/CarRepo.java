package com.nnj.learn.jee.control.repo;

import java.util.List;

import com.nnj.learn.jee.entity.Car;

public  interface CarRepo {

    public void setup();

    public void store(final Car car);

    public Car getCar(final String id);

    public List<Car> getCars(final Integer limit, final Integer offset);

    public void cleanup();
   
}

