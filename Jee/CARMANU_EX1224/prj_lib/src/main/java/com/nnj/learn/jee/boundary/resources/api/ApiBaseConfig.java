package com.nnj.learn.jee.boundary.resources.api;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import com.nnj.learn.jee.boundary.resources.api.v1.CarResource;
import com.nnj.learn.jee.boundary.resources.api.v1.exceptions.mapper.InvalidInputMapper;

@ApplicationPath("api")
public class ApiBaseConfig extends Application{
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(CarResource.class);
        classes.add(InvalidInputMapper.class);
        return classes;
    }
}
