package com.nnj.learn.jee.control;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class BadSpecification extends RuntimeException {
    public BadSpecification() {
        super("Please check your input. EngineType and Color are mandatory attributes.");
    }
}

