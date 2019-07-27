package com.accele.gage.control;

public class MouseMoveEvent {

	private double x;
	private double y;
	
	public MouseMoveEvent(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
}
