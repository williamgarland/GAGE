package com.accele.gage.control;

import com.accele.gage.gfx.Window;

public class ControlEvent {

	private Window window;
	
	public ControlEvent(Window window) {
		this.window = window;
	}
	
	public Window getWindow() {
		return window;
	}
	
}
