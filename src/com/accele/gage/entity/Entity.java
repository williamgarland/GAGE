package com.accele.gage.entity;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;
import com.accele.gage.Indexable;
import com.accele.gage.Renderable;
import com.accele.gage.Tickable;
import com.accele.gage.math.BoundingBox;

/**
 * A general-purpose entity class used for displaying objects to the screen.
 * <p>
 * This class contains a few primary base capabilities: graphical display, logical processing, and collision detection.
 * All instances of {@code Entity} override {@link com.accele.gage.Renderable Renderable} for rendering to the screen,
 * {@link com.accele.gage.Tickable Tickable} for updating {@code Entity} logic, {@link com.accele.gage.Cleanable Cleanable}
 * to free any potential resources the entity might have, and {@link com.accele.gage.Indexable Indexable} to keep track of entity types.
 * </p>
 * <p>
 * Even though {@code Entity} contains a {@code registryId}, it is not used in any particular {@link com.accele.gage.Registry Registry}.
 * Instead, this value is used to track entity types within the {@link com.accele.gage.entity.EntityHandler EntityHandler}, which contains all entities
 * that the engine is currently processing.
 * </p>
 * <p>
 * This class also contains basic collision detection through the use of one of two built-in types of {@link com.accele.gage.math.BoundingBox BoundingBox}: 
 * {@link com.accele.gage.math.AABB AABB} or {@link com.accele.gage.math.OBB OBB}. The {@code EntityHandler} automatically keeps track of {@code Entity} collisions
 * and will call the {@link #collide(Entity)} method when two entities collide.
 * </p>
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Entity implements Indexable, Tickable, Renderable, Cleanable {

	protected String registryId;
	protected BoundingBox bounds;
	protected boolean dead;
	
	/**
	 * Creates a new {@link Entity Entity} with the specified {@code registryId} and {@code bounds}.
	 * 
	 * <p>
	 * To exempt this {@code Entity} from collision detection, either pass in {@code null} for the {@code bounds} parameter 
	 * or set the global setting {@link com.accele.gage.GameConfiguration#setEntityCollision(boolean) setEntityCollision(boolean)} to {@code false}.
	 * </p>
	 * 
	 * @param registryId the {@code registryId} of the {@code Entity}
	 * @param bounds the {@code BoundingBox} of the {@code Entity} used for collision detection
	 */
	public Entity(String registryId, BoundingBox bounds) {
		this.registryId = registryId;
		this.bounds = bounds;
	}
	
	/**
	 * Called whenever this {@code Entity} collides with {@code other} according to its {@link com.accele.gage.math.BoundingBox BoundingBox}.
	 * <p>
	 * Collision detection is done through the use of one of two built-in types of {@link com.accele.gage.math.BoundingBox BoundingBox}: 
	 * {@link com.accele.gage.math.AABB AABB} or {@link com.accele.gage.math.OBB OBB}.
	 * </p>
	 * <p>
	 * To exempt this {@code Entity} from collision detection, either pass in {@code null} for the {@code bounds} parameter 
	 * or set the global setting {@link com.accele.gage.GameConfiguration#setEntityCollision(boolean) setEntityCollision(boolean)} to {@code false}.
	 * </p>
	 * @param other the {@code Entity} that this {@code Entity} is colliding with
	 */
	public abstract void collide(Entity other);
	
	/**
	 * Called whenever this {@code Entity} dies.
	 * <p>
	 * Entities are killed via the {@link #die()} function. {@code die()} will call this method directly before calling {@link #clean()}.
	 * The {@code Entity} will not be removed from the {@code EntityHandler} until the end of the current game loop cycle.
	 * </p>
	 */
	public abstract void onDeath();
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	/**
	 * Returns the {@link com.accele.gage.math.BoundingBox BoundingBox} used for detecting collisions.
	 * 
	 * @return the {@code BoundingBox} used for detecting collisions
	 */
	public BoundingBox getBounds() {
		return bounds;
	}
	
	/**
	 * Returns whether this {@code Entity} is dead.
	 * <p>
	 * Entities are considered dead once their {@link #die()} method has been called. Dead entities are automatically exempt from collision detection.
	 * </p>
	 * 
	 * @return whether this {@code Entity} is dead
	 */
	public boolean isDead() {
		return dead;
	}
	
	/**
	 * Kills this {@code Entity}.
	 * <p>
	 * This method will call {@link #onDeath()} followed by {@link #clean()} and then place the {@code Entity} in the removal queue.
	 * Dead entities are automatically exempt from collision detection.
	 * This method will do nothing if this {@code Entity} is already dead.
	 * The {@code Entity} will not be removed from the {@code EntityHandler} until the end of the current game loop cycle.
	 * </p>
	 */
	public void die() {
		if (!dead) {
			dead = true;
			onDeath();
			GAGE.getInstance().getEntityHandler().removeEntity(this);
			clean();
		}
	}
	
}
