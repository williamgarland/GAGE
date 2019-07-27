package com.accele.gage;

import com.accele.gage.gfx.Graphics;

/**
 * Denotes an object that should be rendered every frame.
 * 
 * Objects will be rendered as many times per second as the running system will allow.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Renderable {

	/**
	 * Called as many times as possible per second to render the object.
	 * 
	 * This method is designed to update object graphics only and should not be used to modify anything relating to object logic.
	 * {@link com.accele.gage.GameConfiguration#getFps getFps()} returns the number of times per second this method will be called.
	 * 
	 * @param g the instance of {@link com.accele.gage.gfx.Graphics Graphics} used by the engine to render objects
	 * @param interpolation the normalized difference in time between when the previous frame was called and when this frame was called
	 * @see com.accele.gage.GameConfiguration GameConfiguration
	 */
	public void render(Graphics g, double interpolation);
	
}
