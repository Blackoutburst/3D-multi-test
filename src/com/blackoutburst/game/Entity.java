package com.blackoutburst.game;

import com.blackoutburst.bogel.maths.Vector3f;

public abstract class Entity {
	
	protected int id;
	protected EntityTypes type;
	protected Vector3f position;
	protected Vector3f lastPosition;
	protected Vector3f newPosition;
	protected Vector3f scale;
	protected Vector3f rotation;
	protected Vector3f velocity;
	
	public Entity(int id, EntityTypes type, Vector3f position, Vector3f scale, Vector3f rotation) {
		this.id = id;
		this.type = type;
		this.position = position;
		this.lastPosition = position.copy();
		this.newPosition = position.copy();
		this.scale = scale;
		this.rotation = rotation;
		this.velocity = new Vector3f();
	}

	protected abstract void update();
	protected abstract void render();
	
	public int getId() {
		return id;
	}

	public Entity setId(int id) {
		this.id = id;
		return (this);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public EntityTypes getType() {
		return type;
	}

	public void setType(EntityTypes type) {
		this.type = type;
	}
	
}
