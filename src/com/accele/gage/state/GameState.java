package com.accele.gage.state;

import com.accele.gage.Cleanable;
import com.accele.gage.Indexable;
import com.accele.gage.Renderable;
import com.accele.gage.Tickable;

public abstract class GameState implements Indexable, Tickable, Renderable, Cleanable {

	protected String registryId;
	
	public GameState(String registryId) {
		this.registryId = registryId;
	}
	
	public abstract void init();
	
	public abstract void exit();
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
}
