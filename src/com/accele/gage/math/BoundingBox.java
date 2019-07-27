package com.accele.gage.math;

public abstract class BoundingBox {

	protected Vector3f position;
	protected Vector3f size;
	
	public BoundingBox(Vector3f position, Vector3f size) {
		this.position = position;
		this.size = size;
	}
	
	public abstract boolean intersects(BoundingBox other);
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getSize() {
		return size;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setSize(Vector3f size) {
		this.size = size;
	}
	
}
