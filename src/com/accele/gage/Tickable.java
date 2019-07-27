package com.accele.gage;

/**
 * Denotes an object that should be updated every tick.
 * 
 * Unless changed using {@link com.accele.gage.GameConfiguration#setTicksPerSecond setTicksPerSecond(double)}, objects will update 25 times per second.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Tickable {

	/**
	 * Called a predetermined number of times per second to update the object.
	 * 
	 * This method is designed to update game logic only and should not be used to modify anything relating to object rendering.
	 * {@link com.accele.gage.GameConfiguration#getTicksPerSecond getTicksPerSecond()} returns the number of times per second this method will be called.
	 * 
	 * @see com.accele.gage.GameConfiguration GameConfiguration
	 */
	public void tick();
	
}
