package com.accele.gage.control;

import com.accele.gage.gfx.Window;

public class MouseWheelEvent extends ControlEvent {

	private double xOffset;
	private double yOffset;
	
	public MouseWheelEvent(Window window, double xOffset, double yOffset) {
		super(window);
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
