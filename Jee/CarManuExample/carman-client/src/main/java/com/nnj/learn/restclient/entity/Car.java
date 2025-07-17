package com.nnj.learn.restclient.entity;

public class Car {
	private String id;
	private EngineType engineType;
	private Color color;

	public Color getColor() {
		return color;
	}

	public EngineType getEngineType() {
		return engineType;
	}

	public String getId() {
		return id;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public void setEngine(final EngineType engineType) {
		this.engineType = engineType;
	}

	public void setEngineType(final EngineType engineType) {
		this.engineType = engineType;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setIdentifier(final String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Car [id=" + id + ", engineType=" + engineType + ", color=" + color + "]";
	}

}
