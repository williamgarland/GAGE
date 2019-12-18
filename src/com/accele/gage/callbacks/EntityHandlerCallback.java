package com.accele.gage.callbacks;

import com.accele.gage.entity.Entity;

/**
 * A callback used for detecting changes in the {@link com.accele.gage.entity.EntityHandler EntityHandler}.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public interface EntityHandlerCallback {

	/**
	 * Invoked whenever a change in the {@link com.accele.gage.entity.EntityHandler EntityHandler} occurs.
	 * Adding and removing entities are the most likely reasons for this method to be invoked.
	 * 
	 * @param e the {@code Entity} that caused the change in the {@code EntityHandler}
	 */
	public void call(Entity e);
	
}
