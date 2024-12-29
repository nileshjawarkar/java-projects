package com.nnj.learn.javaee8.entity;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class InvalidEngine extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public InvalidEngine(String message) {
		super(message);
	}
}
