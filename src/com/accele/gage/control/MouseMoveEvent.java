package com.accele.gage.control;

import com.accele.gage.gfx.Window;

public class MouseMoveEvent extends ControlEvent {

	private double x;
	private double y;
	
	public MouseMoveEvent(Window window, double x, double y) {
		super(window);
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
