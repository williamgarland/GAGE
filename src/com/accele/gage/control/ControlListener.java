package com.accele.gage.control;

import com.accele.gage.Indexable;

public interface ControlListener extends Indexable {

	public boolean canReceiveEvents();
	
	public void setCanReceiveEvents(boolean canReceiveEvents);
	
}
