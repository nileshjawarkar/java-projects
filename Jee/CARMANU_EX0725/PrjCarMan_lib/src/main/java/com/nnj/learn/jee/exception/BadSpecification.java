package com.nnj.learn.jee.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class BadSpecification extends RuntimeException {
    long serialVersionUID = 10001L;

    public BadSpecification(final String message) {
        super(String.format("Invalid input for attributes [%s]", message));
    }
}
