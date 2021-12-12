package com.blackoutburst.core;

public class Cube {

	protected String texture;
	protected Vector3f position;
	protected Vector3f scale;
	protected Vector3f rotation;
	protected Color color;
	
	public Cube(String texture, Vector3f position, Vector3f scale, Vector3f rotation, Color color) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
		this.color = color;
	}

	public String getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getScale() {
		return scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Color getColor() {
		return color;
	}
	
}
