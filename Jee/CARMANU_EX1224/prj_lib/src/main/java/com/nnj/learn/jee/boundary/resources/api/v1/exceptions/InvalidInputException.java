package com.nnj.learn.jee.boundary.resources.api.v1.exceptions;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(final String message) {
        super("Invalid Input - " + message);
    }
}
