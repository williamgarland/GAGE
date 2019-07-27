package com.accele.gage.entity;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;
import com.accele.gage.Indexable;
import com.accele.gage.Renderable;
import com.accele.gage.Tickable;
import com.accele.gage.math.BoundingBox;

public abstract class Entity implements Indexable, Tickable, Renderable, Cleanable {

	protected String registryId;
	protected BoundingBox bounds;
	protected boolean dead;
	
	public Entity(String registryId, BoundingBox bounds) {
		this.registryId = registryId;
		this.bounds = bounds;
	}
	
	public abstract void collide(Entity other);
	
	public abstract void onDeath();
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	public BoundingBox getBounds() {
		return bounds;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void die() {
		if (!dead) {
			dead = true;
			onDeath();
			GAGE.getInstance().getEntityHandler().removeEntity(this);
			clean();
		}
	}
	
}
