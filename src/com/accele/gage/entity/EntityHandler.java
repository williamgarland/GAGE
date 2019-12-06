package com.accele.gage.entity;

import java.util.ArrayList;
import java.util.List;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;
import com.accele.gage.GameConfiguration;
import com.accele.gage.Renderable;
import com.accele.gage.Tickable;
import com.accele.gage.control.ControlListener;
import com.accele.gage.control.KeyListener;
import com.accele.gage.control.MouseListener;
import com.accele.gage.gfx.Graphics;

/**
 * The {@code EntityHandler} class is responsible for managing all instances of {@link com.accele.gage.entity.Entity Entity} used in the engine.
 * <p>
 * Entity logic processing, collision detection, and rendering are handled automatically through this class. 
 * To use this class in a game, call its {@link #tick()} and {@link #render(Graphics, double)} methods in the respective places 
 * within a {@link com.accele.gage.state.GameState GameState} instance.
 * The {@link #clean()} method is automatically called upon exiting the engine; there is no need to call it manually.
 * </p>
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class EntityHandler implements Tickable, Renderable, Cleanable {

	private List<Entity> entities;
	private GameConfiguration config;
	
	/**
	 * Creates a new {@link com.accele.gage.entity.EntityHandler EntityHandler}.
	 * <p>
	 * This constructor should not be called by the user; the {@link com.accele.gage.GAGE GAGE} class already contains an instance of this class.
	 * </p>
	 * @see com.accele.gage.GAGE#getEntityHandler() getEntityHandler()
	 */
	public EntityHandler() {
		this.entities = new ArrayList<>();
		this.config = GAGE.getInstance().getConfig();
	}
	
	public void tick() {
		entities.forEach(e -> e.tick());
		if (config.doEntityCollision()) {
			for (Entity e : entities) {
				for (Entity other : entities) {
					if (e != other && e.bounds != null && other.bounds != null && e.bounds.intersects(other.bounds) && !e.dead && !other.dead) {
						e.collide(other);
						other.collide(e);
					}
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
	
	/**
	 * Adds an {@link com.accele.gage.entity.Entity Entity} to the {@code EntityHandler}.
	 * <p>
	 * This method will wait to add the {@code Entity} until the end of the current game loop cycle.
	 * </p>
	 * 
	 * @param e the {@code Entity} to add
	 */
	public void addEntity(Entity e) {
		GAGE.getInstance().deferEvent(gage -> entities.add(e));
	}
	
	/**
	 * Removes an {@link com.accele.gage.entity.Entity Entity} from the {@code EntityHandler}.
	 * <p>
	 * This method will wait to remove the {@code Entity} until the end of the current game loop cycle.
	 * This method also will not kill the {@code Entity} before removing it, so {@link com.accele.gage.entity.Entity#onDeath() onDeath()} will be ignored.
	 * To properly kill an entity, use {@link com.accele.gage.entity.Entity#die() die()}.
	 * </p>
	 * @param e the {@code Entity} to remove
	 * @see com.accele.gage.entity.Entity#die() die()
	 */
	public void removeEntity(Entity e) {
		GAGE.getInstance().deferEvent(gage -> {
			entities.remove(e);
			if (e instanceof KeyListener)
				gage.getKeyListenerRegistry().removeEntry(e.registryId);
			if (e instanceof MouseListener)
				gage.getMouseListenerRegistry().removeEntry(e.registryId);
			if (e instanceof ControlListener && !(e instanceof KeyListener) && !(e instanceof MouseListener))
				gage.getControlListenerRegistry().removeEntry(e.registryId);
		});
	}
	
	/**
	 * Removes all instances of {@link com.accele.gage.entity.Entity Entity} from the {@code EntityHandler}.
	 * <p>
	 * This method will wait to remove the entities until the end of the current game loop cycle.
	 * This method also will not kill the entities before removing them, so {@link com.accele.gage.entity.Entity#onDeath() onDeath()} will be ignored.
	 * To properly kill an entity, use {@link com.accele.gage.entity.Entity#die() die()}.
	 * </p>
	 * @see com.accele.gage.entity.Entity#die() die()
	 */
	public void removeAllEntities() {
		GAGE.getInstance().deferEvent(gage -> {
			entities.forEach(e -> {
				if (e instanceof KeyListener)
					gage.getKeyListenerRegistry().removeEntry(e.registryId);
				if (e instanceof MouseListener)
					gage.getMouseListenerRegistry().removeEntry(e.registryId);
				if (e instanceof ControlListener && !(e instanceof KeyListener) && !(e instanceof MouseListener))
					gage.getControlListenerRegistry().removeEntry(e.registryId);
			});
			entities.clear();
		});
	}
	
}
