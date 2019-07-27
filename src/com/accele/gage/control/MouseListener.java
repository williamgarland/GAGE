package com.accele.gage.control;

public interface MouseListener extends ControlListener {

	public void mouseMoved(MouseMoveEvent e);
	
	public void mouseButtonPressed(MouseEvent e);
	
	public void mouseButtonReleased(MouseEvent e);
	
	public void mouseButtonClicked(MouseEvent e);
	
	public void mouseWheelMoved(MouseWheelEvent e);
	
}
