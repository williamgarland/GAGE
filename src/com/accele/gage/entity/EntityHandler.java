package com.accele.gage.entity;

import java.util.ArrayList;
import java.util.List;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;
import com.accele.gage.Renderable;
import com.accele.gage.Tickable;
import com.accele.gage.control.ControlListener;
import com.accele.gage.control.KeyListener;
import com.accele.gage.control.MouseListener;
import com.accele.gage.gfx.Graphics;

public class EntityHandler implements Tickable, Renderable, Cleanable {

	private List<Entity> entities;
	
	public EntityHandler() {
		this.entities = new ArrayList<>();
	}
	
	public void tick() {
		entities.forEach(e -> e.tick());
		for (Entity e : entities) {
			for (Entity other : entities) {
				if (e != other && e.bounds.intersects(other.bounds) && !e.dead && !other.dead) {
					e.collide(other);
					other.collide(e);
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g, double interpolation) {
		entities.forEach(e -> e.render(g, interpolation));
	}

	@Override
	public void clean() {
		entities.forEach(e -> e.clean());
	}
	
	public void addEntity(Entity e) {
		GAGE.getInstance().deferEvent(gage -> entities.add(e));
	}
	
	public void removeEntity(Entity e) {
		GAGE.getInstance().deferEvent(gage -> {
			entities.remove(e);
			if (e instanceof KeyListener)
				gage.getKeyListenerRegistry().removeEntry(e.registryId);
			if (e instanceof MouseListener)
				gage.getMouseListenerRegistry().removeEntry(e.registryId);
			if (e instanceof ControlListener)
				gage.getControlListenerRegistry().removeEntry(e.registryId);
		});
	}
	
	public void removeAllEntities() {
		GAGE.getInstance().deferEvent(gage -> {
			entities.forEach(e -> {
				if (e instanceof KeyListener)
					gage.getKeyListenerRegistry().removeEntry(e.registryId);
				if (e instanceof MouseListener)
					gage.getMouseListenerRegistry().removeEntry(e.registryId);
				if (e instanceof ControlListener)
					gage.getControlListenerRegistry().removeEntry(e.registryId);
			});
			entities.clear();
		});
	}
	
}
