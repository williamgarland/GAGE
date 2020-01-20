package com.accele.gage.entity;

import java.util.ArrayList;
import java.util.List;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;
import com.accele.gage.GameConfiguration;
import com.accele.gage.Renderable;
import com.accele.gage.Tickable;
import com.accele.gage.callbacks.EntityHandlerCallback;
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
	private List<EntityHandlerCallback> entityAddCallbacks;
	private List<EntityHandlerCallback> entityRemoveCallbacks;
	
	/**
	 * Creates a new {@link com.accele.gage.entity.EntityHandler EntityHandler} with the specified {@link com.accele.gage.GameConfiguration GameConfiguration}.
	 * <p>
	 * This constructor should not be called by the user; the {@link com.accele.gage.GAGE GAGE} class already contains an instance of this class.
	 * </p>
	 * @param config the {@code GameConfiguration} to use in the {@code EntityHandler}
	 * @see com.accele.gage.GAGE#getEntityHandler() getEntityHandler()
	 */
	public EntityHandler(GameConfiguration config) {
		this.entities = new ArrayList<>();
		this.config = config;
		this.entityAddCallbacks = new ArrayList<>();
		this.entityRemoveCallbacks = new ArrayList<>();
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
	 * 
	 * <p>
	 * This method will wait to add the {@code Entity} until the end of the current game loop cycle.
	 * When the {@code Entity} is actually added to the list, all entity-add callbacks will be invoked.
	 * </p>
	 * 
	 * @param e the {@code Entity} to add
	 */
	public void addEntity(Entity e) {
		GAGE.getInstance().deferEvent(gage -> { entities.add(e); entityAddCallbacks.forEach(c -> c.call(e)); });
	}
	
	/**
	 * Removes an {@link com.accele.gage.entity.Entity Entity} from the {@code EntityHandler}.
	 * <p>
	 * This method will wait to remove the {@code Entity} until the end of the current game loop cycle.
	 * This method also will not kill the {@code Entity} before removing it, so {@link com.accele.gage.entity.Entity#onDeath() onDeath()} will be ignored.
	 * To properly kill an entity, use {@link com.accele.gage.entity.Entity#die() die()}.
	 * When the {@code Entity} is actually removed from the list, all entity-remove callbacks will be invoked.
	 * </p>
	 * @param e the {@code Entity} to remove
	 * @see com.accele.gage.entity.Entity#die() die()
	 */
	public void removeEntity(Entity e) {
		GAGE.getInstance().deferEvent(gage -> {
			entities.remove(e);
			if (e instanceof KeyListener)
				gage.getMainContext().getKeyListenerRegistry().removeEntry(e.registryId);
			if (e instanceof MouseListener)
				gage.getMainContext().getMouseListenerRegistry().removeEntry(e.registryId);
			if (e instanceof ControlListener && !(e instanceof KeyListener) && !(e instanceof MouseListener))
				gage.getMainContext().getControlListenerRegistry().removeEntry(e.registryId);
			entityRemoveCallbacks.forEach(c -> c.call(e));
		});
	}
	
	/**
	 * Removes all instances of {@link com.accele.gage.entity.Entity Entity} from the {@code EntityHandler}.
	 * <p>
	 * This method will wait to remove the entities until the end of the current game loop cycle.
	 * This method also will not kill the entities before removing them, so {@link com.accele.gage.entity.Entity#onDeath() onDeath()} will be ignored.
	 * To properly kill an entity, use {@link com.accele.gage.entity.Entity#die() die()}.
	 * All entity-remove callbacks will be invoked for each entity in the list.
	 * </p>
	 * @see com.accele.gage.entity.Entity#die() die()
	 */
	public void removeAllEntities() {
		GAGE.getInstance().deferEvent(gage -> {
			entities.forEach(e -> {
				if (e instanceof KeyListener)
					gage.getMainContext().getKeyListenerRegistry().removeEntry(e.registryId);
				if (e instanceof MouseListener)
					gage.getMainContext().getMouseListenerRegistry().removeEntry(e.registryId);
				if (e instanceof ControlListener && !(e instanceof KeyListener) && !(e instanceof MouseListener))
					gage.getMainContext().getControlListenerRegistry().removeEntry(e.registryId);
				entityRemoveCallbacks.forEach(c -> c.call(e));
			});
			entities.clear();
		});
	}
	
	/**
	 * Returns the list of entities currently in the {@code EntityHandler}.
	 * 
	 * @return the list of entities currently in the {@code EntityHandler}
	 */
	public List<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Adds an entity-add callback to the {@code EntityHandler}. The callback will be invoked whenever an {@code Entity} is added to the {@code EntityHandler}.
	 * 
	 * @param callback the {@link com.accele.gage.callbacks.EntityHandlerCallback EntityHandlerCallback} to add
	 */
	public void addEntityAddCallback(EntityHandlerCallback callback) {
		entityAddCallbacks.add(callback);
	}
	
	/**
	 * Adds an entity-remove callback to the {@code EntityHandler}. The callback will be invoked whenever an {@code Entity} is removed from the {@code EntityHandler}.
	 * 
	 * @param callback the {@link com.accele.gage.callbacks.EntityHandlerCallback EntityHandlerCallback} to add
	 */
	public void addEntityRemoveCallback(EntityHandlerCallback callback) {
		entityRemoveCallbacks.add(callback);
	}
	
}
