package com.accele.gage;

/**
 * Allows an object to perform any necessary cleanup tasks before being deallocated.
 * 
 * <p>
 * Generally, most classes implement this interface as a way to safely release any system resources before being deallocated.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Cleanable {

	/**
	 * Allows the implementer to perform any necessary cleanup tasks.
	 * For most engine-specific resources, this method should not be called manually as it is usually always
	 * called after {@link com.accele.gage.GAGE#stop stop()} has been called.
	 */
	public void clean();
	
}
