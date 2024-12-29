package com.nnj.learn.javaee8.control;

import com.nnj.learn.javaee8.entity.Color;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;


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
