package com.accele.gage.control;

import com.accele.gage.gfx.Window;

public class MouseEvent extends ControlEvent {

	private int button;
	private int modifiers;
	private double x;
	private double y;
	
	public MouseEvent(Window window, int button, int modifiers, double x, double y) {
		super(window);
		this.button = button;
		this.modifiers = modifiers;
		this.x = x;
		this.y = y;
	}
	
	public int getButton() {
		return button;
	}
	
	public int getModifiers() {
		return modifiers;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
}
