package com.accele.gage.control;

public class MouseEvent {

	private int button;
	private int modifiers;
	
	public MouseEvent(int button, int modifiers) {
		this.button = button;
		this.modifiers = modifiers;
	}
	
	public int getButton() {
		return button;
	}
	
	public int getModifiers() {
		return modifiers;
	}
	
}
