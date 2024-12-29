package com.nnj.learn.jee.control.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.nnj.learn.jee.entity.Car;

public class CarRepoInMem implements CarRepo {
    private final HashMap<String, Car> store = new HashMap<>();

    @Override
    public void store(final Car car) {
        store.put(car.getIdentifier(), car);
    }

    @Override
    public Car getCar(final String id) {
        return store.get(id);
    }

    @Override
    public List<Car> getCars(final Integer limit, final Integer offset) {
        final List<Car> list = new ArrayList<>();
        for (final Entry<String, Car> entrySet : store.entrySet()) {
            list.add(entrySet.getValue());    
        }
        return list;
    }

    @Override
    public void cleanup() {
        store.clear();
    }

    @Override
    public void setup() {
    }
}

