package com.accele.gage.control;

public class MouseWheelEvent {

	private double xOffset;
	private double yOffset;
	
	public MouseWheelEvent(double xOffset, double yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public double getXOffset() {
		return xOffset;
	}
	
	public double getYOffset() {
		return yOffset;
	}
	
}
