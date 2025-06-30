package com.nnj.learn.javaee8.control;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import com.nnj.learn.javaee8.entity.Color;


public class DefaultColorProducer {

	@Produces
	@Named("Commom")
	public Color getDefaultColor() {
		return Color.RED;
	}
	
	@Produces
	//@Named("SpeatialEdition")
	@SpeatialEditionColor
	public Color getSpeatialEditionColor() {
		return Color.BLUE;
	}
}
